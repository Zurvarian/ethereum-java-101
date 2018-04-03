package ether.wallet.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class UnlockWalletDto {

    @NotNull
    @Size(min = 1)
    String pathToWalletFile;

    @NotNull
    @Size(min = 1)
    String password;

}
