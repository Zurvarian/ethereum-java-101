package ether.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ether.ethereum.EthereumClient;
import ether.wallet.model.Wallet;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.WalletFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Supplier;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PRIVATE;
import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.MnemonicUtils.generateMnemonic;
import static org.web3j.crypto.MnemonicUtils.generateSeed;
import static org.web3j.crypto.TransactionEncoder.signMessage;
import static org.web3j.crypto.Wallet.createStandard;
import static org.web3j.crypto.Wallet.decrypt;
import static org.web3j.utils.Convert.Unit.ETHER;
import static org.web3j.utils.Numeric.toHexString;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WalletService {

    private static final Path PROJECT_BASE = Paths.get("development/wallets");

    private static final String FILE_NAME_FORMAT = "UTC--%s--%s";

    private static final DateTimeFormatter FILE_NAME_DATE_FORMATTER = ofPattern("yyyy-MM-dd'T'HH-mm-ss.nVV");

    WalletRepository walletRepository;

    EthereumClient ethereumClient;

    ObjectMapper objectMapper;

    Wallet createWallet(String password) {

        val ecKeyPair = Try.of(Keys::createEcKeyPair).getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);
        val walletFile = Try.of(() -> createStandard(password, ecKeyPair)).getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);
        val pathToWalletFile = PROJECT_BASE.resolve(walletFileName(walletFile)).toAbsolutePath();

        val wallet = new Wallet(UUID.fromString(walletFile.getId()), Credentials.create(ecKeyPair), walletFile, pathToWalletFile);

        walletRepository.save(wallet);
        writeWalletFileToPath(pathToWalletFile, walletFile);

        return wallet;
    }

    String generateWalletMnemonics() {

        val secureRandom = new SecureRandom();
        val initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        return generateMnemonic(initialEntropy);
    }

    Wallet createWalletWithMnemonics(String password, String mnemonics) {

        val seed = generateSeed(mnemonics, "");
        val ecKeyPair = ECKeyPair.create(sha256(seed));
        val walletFile = Try.of(() -> createStandard(password, ecKeyPair)).getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);
        val pathToWalletFile = PROJECT_BASE.resolve(walletFileName(walletFile)).toAbsolutePath();

        enforceUUIDBasedOnAddressWhenWalletIsCreatedWithMnemonics(walletFile);

        val wallet = new Wallet(UUID.fromString(walletFile.getId()), Credentials.create(ecKeyPair), walletFile, pathToWalletFile);

        walletRepository.save(wallet);
        writeWalletFileToPath(pathToWalletFile, walletFile);

        return wallet;
    }

    Wallet findWalletById(UUID walletId) {
        return walletRepository.findWalletById(walletId).getOrElseThrow(() -> new RuntimeException(String.format("Wallet not found with walletId=%s", walletId)));
    }

    Wallet unlockWallet(Path pathToWalletFile, String password) {

        val absolutePathToWalletFile = absoluteWalletPathFromProjectBaseOrValueIfAbsolute(pathToWalletFile);

        val walletFile = readWalletFileFromPath(absolutePathToWalletFile);
        val credentials = Try.of(() -> decrypt(password, walletFile)).map(Credentials::create).getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);

        val wallet = new Wallet(UUID.fromString(walletFile.getId()), credentials, walletFile, absolutePathToWalletFile);

        walletRepository.save(wallet);

        return wallet;
    }

    String signWithWallet(UUID walletId, RawTransaction rawTransaction) {
        val wallet = walletRepository.findWalletById(walletId).getOrElseThrow(() -> new RuntimeException(String.format("Wallet not found with walletId=%s", walletId)));

        return toHexString(signMessage(rawTransaction, wallet.getCredentials()));
    }

    BigDecimal findBalanceOfWalletById(UUID walletId) {

        val wallet = walletRepository.findWalletById(walletId).getOrElseThrow(() -> new RuntimeException(String.format("Wallet not found with walletId=%s", walletId)));

        return ethereumClient.findBalanceOfAddress(wallet.getCredentials().getAddress(), ETHER);
    }

    private String walletFileName(WalletFile walletFile) {
        return String.format(FILE_NAME_FORMAT, ZonedDateTime.now(UTC).format(FILE_NAME_DATE_FORMATTER), walletFile.getAddress());
    }

    private void writeWalletFileToPath(Path pathToWalletFile, WalletFile walletFile) {
        try {
            objectMapper.writeValue(pathToWalletFile.toFile(), walletFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WalletFile readWalletFileFromPath(Path pathToWalletFile) {
        return Try.of(() -> objectMapper.readValue(pathToWalletFile.toFile(), WalletFile.class)).getOrElseThrow((Supplier<RuntimeException>) RuntimeException::new);
    }

    private Path absoluteWalletPathFromProjectBaseOrValueIfAbsolute(Path pathToWalletFile) {
        return pathToWalletFile.isAbsolute() ? pathToWalletFile : PROJECT_BASE.resolve(pathToWalletFile).toAbsolutePath();
    }

    private void enforceUUIDBasedOnAddressWhenWalletIsCreatedWithMnemonics(WalletFile walletFile) {
        walletFile.setId(UUID.nameUUIDFromBytes(walletFile.getAddress().getBytes()).toString());
    }

}
