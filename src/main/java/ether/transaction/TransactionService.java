package ether.transaction;

import ether.ethereum.EthereumClient;
import ether.transaction.model.CreateTransaction;
import ether.transaction.model.Transaction;
import ether.wallet.WalletRepository;
import ether.wallet.model.Wallet;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.web3j.crypto.RawTransaction;

import java.math.BigInteger;
import java.util.UUID;
import java.util.function.Supplier;

import static ether.transaction.model.Transaction.NO_BLOCK_HASH;
import static ether.transaction.model.Transaction.NO_BLOCK_NUMBER;
import static ether.transaction.model.Transaction.NO_HASH;
import static ether.transaction.model.Transaction.NO_SIGNATURE;
import static ether.transaction.model.Transaction.NO_TRANSACTION_INDEX;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;
import static org.web3j.crypto.TransactionEncoder.signMessage;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;
import static org.web3j.utils.Numeric.toHexString;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TransactionService {

    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(25000);

    TransactionRepository transactionRepository;

    WalletRepository walletRepository;

    EthereumClient ethereumClient;

    Transaction createTransaction(CreateTransaction createTransaction) {

        val wallet = findWalletByIdOrThrow(createTransaction.getWalletId());

        val transaction = createTransactionToTransaction(createTransaction,
                wallet.getCredentials()::getAddress,
                () -> ethereumClient.findTransactionCountByAddress(wallet.getCredentials().getAddress(), LATEST),
                ethereumClient::findGasPrice,
                () -> GAS_LIMIT);

        transactionRepository.save(transaction);

        return transaction;
    }

    Transaction signTransaction(UUID transactionId, String signature) {

        val transactionWithSignature = findTransactionByIdOrThrow(transactionId).withSignature(signature);

        transactionRepository.save(transactionWithSignature.withSignature(signature));

        return transactionWithSignature;
    }

    Transaction signTransactionWithWallet(UUID transactionId) {

        val transaction = findTransactionByIdOrThrow(transactionId);
        val wallet = findWalletByIdOrThrow(transaction.getWalletId());

        val signature = toHexString(signMessage(transactionToRawTransaction(transaction), wallet.getCredentials()));
        val transactionWithSignature = transaction.withSignature(signature);

        transactionRepository.save(transactionWithSignature);

        return transactionWithSignature;
    }

    Transaction broadcastTransaction(UUID transactionId) {

        val transaction = findTransactionByIdOrThrow(transactionId);

        val txHash = ethereumClient.broadcastTransaction(transaction.getSignature());
        val transactionWithHash = transaction.withHash(txHash);

        transactionRepository.save(transactionWithHash);

        return transactionWithHash;
    }

    Transaction findTransactionById(UUID transactionId) {
        return findTransactionByIdOrThrow(transactionId);
    }

    List<Transaction> findTransactionsByWalletId(UUID walletId) {
        return transactionRepository.findByWalletId(walletId);
    }

    private Transaction findTransactionByIdOrThrow(UUID transactionId) {
        return transactionRepository.findById(transactionId).getOrElseThrow(() -> new RuntimeException(String.format("Transaction with id=%s not found", transactionId)));
    }

    private Wallet findWalletByIdOrThrow(UUID walletId) {
        return walletRepository.findWalletById(walletId).getOrElseThrow(() -> new RuntimeException(String.format("Wallet with id=%s not found", walletId)));
    }

    private Transaction createTransactionToTransaction(CreateTransaction createTransaction, Supplier<String> fromSupplier, Supplier<BigInteger> nonceSupplier,
                                                       Supplier<BigInteger> gasPriceSupplier, Supplier<BigInteger> gasSupplier) {
        return new Transaction(
                randomUUID(),
                createTransaction.getWalletId(),
                fromSupplier.get(),
                createTransaction.getTo(),
                createTransaction.getValue(),
                gasPriceSupplier.get(),
                gasSupplier.get(),
                createTransaction.getReference(),
                nonceSupplier.get(),
                toHexString(String.valueOf(createTransaction.getReference()).getBytes()),
                NO_SIGNATURE,
                NO_HASH,
                NO_BLOCK_HASH,
                NO_BLOCK_NUMBER,
                NO_TRANSACTION_INDEX
        );
    }

    private RawTransaction transactionToRawTransaction(Transaction transaction) {
        return RawTransaction.createTransaction(
                transaction.getNonce(),
                transaction.getGasPrice(),
                transaction.getGas(),
                transaction.getTo(),
                transaction.getValue(),
                transaction.getInput()
        );
    }
}
