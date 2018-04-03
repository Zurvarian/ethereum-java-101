package ether.ethereum;

import ether.ethereum.model.EthTransaction;
import lombok.experimental.UtilityClass;
import org.web3j.protocol.core.methods.response.Transaction;

@UtilityClass
public class EthTransactionFunctions {

    static EthTransaction responseTransactionToEthTransaction(Transaction transaction) {
        return new EthTransaction(
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
        );
    }
}
