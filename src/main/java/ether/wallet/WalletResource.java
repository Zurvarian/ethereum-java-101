package ether.wallet;

import ether.wallet.dto.BalanceDto;
import ether.wallet.dto.CreateWalletDto;
import ether.wallet.dto.MnemonicsDto;
import ether.wallet.dto.SignatureRequestDto;
import ether.wallet.dto.SignatureResponseDto;
import ether.wallet.dto.UnlockWalletDto;
import ether.wallet.dto.WalletDto;
import ether.wallet.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.file.Paths;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.web3j.crypto.RawTransaction.createTransaction;

@RestController
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(path = "/wallet", produces = APPLICATION_JSON_VALUE)
public class WalletResource {

    WalletService walletService;

    @GetMapping(path = "/mnemonic")
    public MnemonicsDto generateWalletMnemonics() {
        return new MnemonicsDto(walletService.generateWalletMnemonics());
    }

    @PostMapping(path = "/mnemonic", consumes = APPLICATION_JSON_VALUE)
    public WalletDto createWalletWithMnemonics(@Valid @RequestBody CreateWalletDto createWalletDto) {
        return walletToDto(walletService.createWalletWithMnemonics(createWalletDto.getPassword(), createWalletDto.getMnemonics()));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public WalletDto createWallet(@Valid @RequestBody CreateWalletDto createWalletDto) {
        return walletToDto(walletService.createWallet(createWalletDto.getPassword()));
    }

    @GetMapping(path = "/{walletId}")
    public WalletDto findWalletById(@PathVariable("walletId") UUID walletId) {
        return walletToDto(walletService.findWalletById(walletId));
    }

    @PostMapping(path = "/unlock")
    public WalletDto unlockWallet(@Valid @RequestBody UnlockWalletDto unlockWalletDto) {
        return walletToDto(walletService.unlockWallet(Paths.get(unlockWalletDto.getPathToWalletFile()), unlockWalletDto.getPassword()));
    }

    @PutMapping(path = "/{walletId}/signature")
    public SignatureResponseDto signWithWallet(@PathVariable("walletId") UUID walletId, @Valid @RequestBody SignatureRequestDto signatureRequestDto) {
        return new SignatureResponseDto(walletService.signWithWallet(walletId, createTransaction(
                signatureRequestDto.getNonce(),
                signatureRequestDto.getGasPrice(),
                signatureRequestDto.getGas(),
                signatureRequestDto.getTo(),
                signatureRequestDto.getValue(),
                signatureRequestDto.getInput()
        )));
    }

    @GetMapping(path = "/{walletId}/balance")
    public BalanceDto findBalanceOfWalletById(@PathVariable("walletId") UUID walletId) {
        return new BalanceDto(walletService.findBalanceOfWalletById(walletId));
    }

    private WalletDto walletToDto(Wallet wallet) {
        return new WalletDto(wallet.getWalletId(), wallet.getCredentials().getAddress(),
                wallet.getCredentials().getEcKeyPair().getPrivateKey(), wallet.getCredentials().getEcKeyPair().getPublicKey(),
                String.valueOf(wallet.getPathToWalletFile()));
    }

}
