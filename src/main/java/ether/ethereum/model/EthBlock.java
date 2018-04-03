package ether.ethereum.model;

import io.vavr.collection.List;
import lombok.Value;

import java.math.BigInteger;

@Value
public class EthBlock {

    BigInteger number;

    String hash;

    String parentHash;

    BigInteger nonce;

    String sha3Uncles;

    String logsBloom;

    String transactionsRoot;

    String stateRoot;

    String receiptsRoot;

    String author;

    String miner;

    String mixHash;

    BigInteger difficulty;

    BigInteger totalDifficulty;

    String extraData;

    BigInteger size;

    BigInteger gasLimit;

    BigInteger gasUsed;

    BigInteger timestamp;

    List<String> transactionHashes;

    List<String> uncles;

    List<String> sealFields;
}
