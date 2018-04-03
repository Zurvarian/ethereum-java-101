package ether.ethereum;

import ether.ethereum.model.EthTransaction;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import rx.Observable;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class EthereumObserverConfig {

    Web3j web3j;

    @Bean
    Observable<EthTransaction> transactionObservable() {
        return web3j.transactionObservable()
                .map(transaction ->
                        Try.of(() ->
                                new EthTransaction(
                                        transaction.getFrom(),
                                        transaction.getTo(),
                                        transaction.getValue(),
                                        transaction.getGasPrice(),
                                        transaction.getGas(),
                                        transaction.getNonce(),
                                        transaction.getInput(),
                                        transaction.getR(),
                                        transaction.getS(),
                                        transaction.getV(),
                                        transaction.getHash(),
                                        transaction.getBlockHash(),
                                        transaction.getBlockNumber(),
                                        transaction.getTransactionIndex()
                                ))
                                .onFailure(throwable -> log.error("Error parsing EthTransaction ", throwable))
                                .getOrNull()
                );
    }

}
