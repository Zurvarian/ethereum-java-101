package ether.wallet.model;

import lombok.Value;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletFile;

import java.nio.file.Path;
import java.util.UUID;

@Value
public class Wallet {

    UUID walletId;

    Credentials credentials;

    WalletFile walletFile;

    Path pathToWalletFile;

}
