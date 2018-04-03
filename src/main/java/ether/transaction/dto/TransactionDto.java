package ether.transaction.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.UUID;

@Value
public class TransactionDto {

    @NotNull
    UUID id;

    @NotNull
    UUID walletId;

    @NotNull
    @Size(min = 40, max = 42)
    String from;

    @NotNull
    @Size(min = 40, max = 42)
    String to;

    @NotNull
    @Min(0)
    BigInteger value;

    @NotNull
    @Min(0)
    BigInteger gasPrice;

    @NotNull
    @Min(0)
    BigInteger gas;

    @NotNull
    UUID reference;

    @NotNull
    @Min(0)
    BigInteger nonce;

    @NotNull
    String input;

    @Size(min = 1)
    String signature;

    @Size(min = 1)
    String hash;

    @Size(min = 1)
    String blockHash;

    @Min(0)
    BigInteger blockNumber;

    @Min(0)
    BigInteger transactionIndex;

}
