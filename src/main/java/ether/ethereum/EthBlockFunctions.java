package ether.ethereum;

import ether.ethereum.model.EthBlock;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.experimental.UtilityClass;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionHash;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.function.Function;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;

@UtilityClass
public class EthBlockFunctions {

    static EthBlock blockToEthBlock(Block block) {
        return new EthBlock(
                block.getNumber(),
                block.getHash(),
                block.getParentHash(),
                block.getNonce(),
                block.getSha3Uncles(),
                block.getLogsBloom(),
                block.getTransactionsRoot(),
                block.getStateRoot(),
                block.getReceiptsRoot(),
                block.getAuthor(),
                block.getMiner(),
                block.getMixHash(),
                block.getDifficulty(),
                block.getTotalDifficulty(),
                block.getExtraData(),
                block.getSize(),
                block.getGasLimit(),
                block.getGasUsed(),
                block.getTimestamp(),
                transactionHashesFromBlockTransactions(block),
                List.ofAll(Option.of(block.getUncles()).getOrElse(emptyList())),
                List.ofAll(Option.of(block.getSealFields()).getOrElse(emptyList()))
        );
    }

    private List<String> transactionHashesFromBlockTransactions(org.web3j.protocol.core.methods.response.EthBlock.Block block) {
        return List.ofAll(Option.of(block.getTransactions()).getOrElse(emptyList())).map(transactionResultToEitherHashOrTransaction()).map(eitherHashOrObject -> eitherHashOrObject.fold(identity(), Transaction::getHash));
    }

    private Function<org.web3j.protocol.core.methods.response.EthBlock.TransactionResult, Either<String, Transaction>> transactionResultToEitherHashOrTransaction() {
        return transactionResult -> transactionResult instanceof TransactionHash ? left((String) transactionResult.get()) : right(((TransactionObject) transactionResult.get()).get());
    }
}
