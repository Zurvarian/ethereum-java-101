package ether.ethereum;

import ether.ethereum.model.EthBlock;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static lombok.AccessLevel.PRIVATE;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;
import static org.web3j.utils.Convert.fromWei;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class EthereumClient {

    Web3j web3j;

    public EthBlock findBlockByParameter(@NonNull DefaultBlockParameter blockParameter) {
        return Try.of(() -> web3j.ethGetBlockByNumber(blockParameter, true).send())
                .flatMap(mapResponseToTryWithSuccessMapper(org.web3j.protocol.core.methods.response.EthBlock::getBlock))
                .map(EthBlockFunctions::blockToEthBlock)
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    public BigDecimal findBalanceOfAddress(@NonNull String address, @NonNull Convert.Unit unit) {
        return Try.of(() -> web3j.ethGetBalance(address, LATEST).send())
                .flatMap(mapResponseToTryWithSuccessMapper(EthGetBalance::getBalance))
                .map(amount -> fromWei(new BigDecimal(amount), unit))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    public BigInteger findTransactionCountByAddress(@NonNull String address, @NonNull DefaultBlockParameter blockParameter) {
        return Try.of(() -> web3j.ethGetTransactionCount(address, blockParameter).send())
                .flatMap(mapResponseToTryWithSuccessMapper(EthGetTransactionCount::getTransactionCount))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    public BigInteger findGasPrice() {
        return Try.of(() -> web3j.ethGasPrice().send())
                .flatMap(mapResponseToTryWithSuccessMapper(EthGasPrice::getGasPrice))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    public String broadcastTransaction(String transactionAsHex) {
        return Try.of(() -> web3j.ethSendRawTransaction(transactionAsHex).send())
                .flatMap(mapResponseToTryWithSuccessMapper(EthSendTransaction::getTransactionHash))
                .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    private <T, S extends Response<T>, R> Function<S, Try<R>> mapResponseToTryWithSuccessMapper(Function<S, R> successMapper) {
        return response -> response.hasError() ? failure(new RuntimeException(response.getError().getMessage())) : success(successMapper.apply(response));
    }

}
