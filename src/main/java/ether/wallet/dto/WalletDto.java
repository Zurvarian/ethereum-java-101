package ether.wallet.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.UUID;

@Value
public class WalletDto {

    @NotNull
    UUID walletId;

    @NotNull
    @Size(min = 1)
    String address;

    @NotNull
    @Min(0)
    BigInteger privateKey;

    @NotNull
    @Min(0)
    BigInteger publicKey;

    @NotNull
    @Min(0)
    String pathToWalletFile;

}
