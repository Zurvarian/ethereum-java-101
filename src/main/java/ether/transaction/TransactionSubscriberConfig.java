package ether.transaction;

import ether.ethereum.model.EthTransaction;
import ether.transaction.model.Transaction;
import ether.wallet.WalletRepository;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.Observable;
import rx.Subscription;

import java.util.function.Function;

import static ether.transaction.TransactionFunctions.buildTransactionFromWalletAndEthTransaction;
import static ether.transaction.TransactionFunctions.referenceFromInput;
import static java.util.UUID.randomUUID;
import static java.util.function.Function.identity;
import static lombok.AccessLevel.PRIVATE;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TransactionSubscriberConfig {

    WalletRepository walletRepository;

    TransactionRepository transactionRepository;

    @Bean
    Subscription transactionSubscription(Observable<EthTransaction> transactionObservable) {
        return transactionObservable.subscribe(ethTransaction ->
                Try.of(() ->
                        walletRepository.findWalletByAddress(ethTransaction.getFrom())
                                .onEmpty(() -> log.debug(String.format("Transaction txHash=%s from=%s does not match with any of our wallets", ethTransaction.getHash(), ethTransaction.getFrom())))
                                .peek(wallet -> log.info(String.format("Transaction txHash=%s from=%s matched with walletId=%s", ethTransaction.getHash(), ethTransaction.getFrom(), wallet.getWalletId())))
                                .map(wallet -> new Tuple2<>(wallet, referenceFromInput(ethTransaction.getInput()).flatMap(transactionRepository::findByReference)))
                                .map(walletAndTransaction -> walletAndTransaction.map(identity(), transaction -> transaction.map(fillTransactionWithEthTransaction(ethTransaction)).getOrElse(() -> buildTransactionFromWalletAndEthTransaction(randomUUID(), walletAndTransaction._1, ethTransaction))))
                                .peek(walletAndTransactionReceive -> log.info("WalletId={} walletAddress={} transactionId={} transactionHash={} transactionBlockHash={} transactionBlockNumber={}", walletAndTransactionReceive._1.getWalletId(), walletAndTransactionReceive._1.getCredentials().getAddress(),
                                        walletAndTransactionReceive._2.getId(), walletAndTransactionReceive._2.getHash(), walletAndTransactionReceive._2.getBlockHash(), walletAndTransactionReceive._2.getBlockNumber()))
                                .map(Tuple2::_2)
                                .peek(transactionRepository::save)
                ).onFailure(throwable -> log.error("Error in TransactionSubscription ", throwable))
        );
    }

    private Function<Transaction, Transaction> fillTransactionWithEthTransaction(EthTransaction ethTransaction) {
        return transaction -> transaction.withBlockHash(ethTransaction.getBlockHash()).withBlockNumber(ethTransaction.getBlockNumber()).withTransactionIndex(ethTransaction.getTransactionIndex());
    }
}
