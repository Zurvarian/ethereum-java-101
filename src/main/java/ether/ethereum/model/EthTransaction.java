package ether.ethereum.model;

import lombok.NonNull;
import lombok.Value;

import java.math.BigInteger;

@Value
public class EthTransaction {

    @NonNull
    String from;

    String to;

    @NonNull
    BigInteger value;

    @NonNull
    BigInteger gasPrice;

    @NonNull
    BigInteger gas;

    @NonNull
    BigInteger nonce;

    @NonNull
    String input;

    @NonNull
    String r;

    @NonNull
    String s;

    @NonNull
    Integer v;

    @NonNull
    String hash;

    String blockHash;

    BigInteger blockNumber;

    BigInteger transactionIndex;

}
