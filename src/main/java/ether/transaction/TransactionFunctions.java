package ether.transaction;

import ether.ethereum.model.EthTransaction;
import ether.transaction.model.Transaction;
import ether.wallet.model.Wallet;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

import static ether.transaction.model.Transaction.NO_REFERENCE;
import static ether.transaction.model.Transaction.NO_SIGNATURE;
import static ether.transaction.model.TransactionType.RECEIVE;
import static ether.transaction.model.TransactionType.SEND;
import static org.web3j.utils.Numeric.hexStringToByteArray;

@UtilityClass
@Slf4j
class TransactionFunctions {

    static Option<UUID> referenceFromInput(String input) {
        return Try.of(() -> new String(hexStringToByteArray(input), "UTF-8")).map(UUID::fromString).toOption();
    }

    static Transaction buildTransactionFromWalletAndEthTransaction(UUID transactionId, Wallet wallet, EthTransaction ethTransaction) {
        return new Transaction(
                transactionId,
                wallet.getWalletId(),
                ethTransaction.getFrom(),
                ethTransaction.getTo(),
                ethTransaction.getValue(),
                ethTransaction.getGasPrice(),
                ethTransaction.getGas(),
                NO_REFERENCE,
                ethTransaction.getNonce(),
                ethTransaction.getInput(),
                Objects.equals(ethTransaction.getFrom(), wallet.getCredentials().getAddress()) ? SEND : RECEIVE,
                NO_SIGNATURE,
                ethTransaction.getHash(),
                ethTransaction.getBlockHash(),
                ethTransaction.getBlockNumber(),
                ethTransaction.getTransactionIndex()
        );
    }
}
