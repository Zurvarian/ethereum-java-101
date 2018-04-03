package ether.transaction.model;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

import java.math.BigInteger;
import java.util.UUID;

@Value
public class Transaction {

    public static UUID NO_REFERENCE = UUID.fromString("00000000-00000-0000-0000-00000000");

    public static String NO_SIGNATURE = null;

    public static String NO_HASH = null;

    public static String NO_BLOCK_HASH = null;

    public static BigInteger NO_BLOCK_NUMBER = null;

    public static BigInteger NO_TRANSACTION_INDEX = null;

    @NonNull
    UUID id;

    @NonNull
    UUID walletId;

    @NonNull
    String from;

    @NonNull
    String to;

    @NonNull
    BigInteger value;

    @NonNull
    BigInteger gasPrice;

    @NonNull
    BigInteger gas;

    @NonNull
    UUID reference;

    @NonNull
    BigInteger nonce;

    @NonNull
    String input;

    @Wither
    String signature;

    @Wither
    String hash;

    @Wither
    String blockHash;

    @Wither
    BigInteger blockNumber;

    @Wither
    BigInteger transactionIndex;

}
