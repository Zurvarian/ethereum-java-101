package ether.transaction.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.UUID;

@Value
public class CreateTransactionDto {

    @NotNull
    UUID walletId;

    @NotNull
    @Size(min = 40, max = 42)
    String to;

    @NotNull
    @Min(1)
    BigInteger value;

    @NotNull
    UUID reference;

}
