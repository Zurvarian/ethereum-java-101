package ether.wallet;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.val;
import org.junit.Ignore;
import org.junit.Test;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import static io.vavr.control.Option.ofOptional;
import static java.math.BigInteger.ONE;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.web3j.crypto.WalletUtils.loadCredentials;
import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;
import static org.web3j.utils.Convert.Unit.ETHER;
import static org.web3j.utils.Convert.fromWei;
import static org.web3j.utils.Convert.toWei;
import static org.web3j.utils.Numeric.hexStringToByteArray;

public class WalletContractIT {

    private static final BigInteger ONE_HUNDRED_ETHER_AS_WEI = new BigInteger("100000000000000000000");

    // TODO Change this address if you ever destroy and recreate the contract
//    private static final String CONTRACT_ADDRESS = "0xc2854ac60B406F90d5Ff6B3F0cdb423B4CEE1868";
//    private static final String CONTRACT_ADDRESS = "0x8644b88fa6325895c7067f1815b66797756bd7f4";
    private static final String CONTRACT_ADDRESS = "0x191bca951e29b66fd57a944e79a72b5f12f4e22d";

    //    private static final String CONTRACT_OWNER_WALLET_FILE = "UTC--2018-01-25T15-38-56.617000000Z--da333dab7975708612e0adc271f88f78d8e35157";
    private static final String CONTRACT_OWNER_WALLET_FILE = "UTC--2018-02-06T09-11-30.523000000Z--3f73bd9eb7b41b9cae5480d700dc646ac39ca787";

    private static final String CONTRACT_OWNER_PASSWORD = "12345";

    private static final String WALLET_OWNER = "0xda333dab7975708612e0adc271f88f78d8e35157";

    @Ignore
    @Test
    public void deployContract() throws Exception {

        val credentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        val walletContract = WalletContract.deploy(web3j, credentials, calculateGasPrice(web3j), GAS_LIMIT, singletonList(WALLET_OWNER), ONE, ONE_HUNDRED_ETHER_AS_WEI).send();

        System.out.println("Wallet Contract deployed=" + walletContract.isValid() + " with address=" + walletContract.getContractAddress() + " by transaction=" + ofOptional(walletContract.getTransactionReceipt()).map(TransactionReceipt::getTransactionHash).getOrNull());
    }

    @Ignore
    @Test
    public void destroyContract() throws Exception {

        val credentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        // TODO Probably Contract.GAS_LIMIT is exaggerated for executing contract functions, but to calculate the accurate value could be tricky
        val walletContract = WalletContract.load(CONTRACT_ADDRESS, web3j, credentials, calculateGasPrice(web3j), GAS_LIMIT);

        val transactionReceipt = walletContract.kill(credentials.getAddress()).send();
        System.out.println("Wallet destroyed=" + !walletContract.isValid() + " by transaction=" + transactionReceipt.getTransactionHash());
    }

    @Ignore
    @Test
    public void depositIntoContract() throws IOException, CipherException, TransactionException {

        val contractCreatorCredentials = loadContractCreatorCredentials();

        val web3j = createLocalWeb3j();
        val gasPrice = calculateGasPrice(web3j);
        val transactionReceiptProcessor = new PollingTransactionReceiptProcessor(web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);

        val fundingAmount = "0.00015";

        val transactionManager = new RawTransactionManager(web3j, contractCreatorCredentials);
        val fundingTransaction = transactionManager.sendTransaction(gasPrice, BigInteger.valueOf(25000L), CONTRACT_ADDRESS, "", toWei(fundingAmount, ETHER).toBigIntegerExact());
        val transactionReceipt = transactionReceiptProcessor.waitForTransactionReceipt(fundingTransaction.getTransactionHash());

        System.out.println("Wallet funded with amount=" + fundingAmount + " by transaction=" + transactionReceipt.getTransactionHash());
    }

    @Ignore
    @Test
    public void executeTransaction() throws Exception {

        val contractCreatorCredentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        val walletContract = WalletContract.load(CONTRACT_ADDRESS, web3j, contractCreatorCredentials, calculateGasPrice(web3j), GAS_LIMIT);

        val reference = randomUUID();
        val amountInEther = "0.0001";
        // toHexString(String.valueOf(reference).getBytes()).getBytes()
        val transactionReceipt = walletContract.execute(WALLET_OWNER, toWei("0.0001", ETHER).toBigIntegerExact(), new byte[]{}).send();

        System.out.println(String.format("Send transfer from wallet contract to=%s with amount=%s and reference=%s by transaction=%s", contractCreatorCredentials.getAddress(), amountInEther, reference, transactionReceipt.getTransactionHash()));
    }

    @Ignore
    @Test
    public void walletContractIsValid() throws IOException, CipherException {

        val credentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        val walletContract = WalletContract.load(CONTRACT_ADDRESS, web3j, credentials, calculateGasPrice(web3j), GAS_LIMIT);

        System.out.println("Is Wallet contract valid=" + walletContract.isValid());
    }

    @Ignore
    @Test
    public void observeDepositEvents() throws IOException, CipherException {

        val credentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        val walletContract = WalletContract.load(CONTRACT_ADDRESS, web3j, credentials, calculateGasPrice(web3j), GAS_LIMIT);

        walletContract.depositEventObservable(EARLIEST, LATEST).subscribe(depositEventResponse -> System.out.println("DepositEvent from=" + depositEventResponse._from + " amount=" + fromWei(new BigDecimal(depositEventResponse.value), ETHER)));
    }

    @Ignore
    @Test
    public void observeSingleTransactEvents() throws IOException, CipherException {

        val credentials = loadContractCreatorCredentials();
        val web3j = createLocalWeb3j();

        val walletContract = WalletContract.load(CONTRACT_ADDRESS, web3j, credentials, calculateGasPrice(web3j), GAS_LIMIT);

        walletContract.singleTransactEventObservable(EARLIEST, LATEST).subscribe(singleTransactEvent ->
                System.out.println(String.format("SingleTransactEvent from=%s amount=%s to=%s reference=%s",
                        singleTransactEvent.owner, fromWei(new BigDecimal(singleTransactEvent.value), ETHER), singleTransactEvent.to, referenceFromInput(singleTransactEvent.data).getOrNull())));
    }

    private Credentials loadContractCreatorCredentials() throws IOException, CipherException {
        return loadCredentials(CONTRACT_OWNER_PASSWORD, WalletContractIT.class.getClassLoader().getResource(CONTRACT_OWNER_WALLET_FILE).getFile());
    }

    private Web3j createLocalWeb3j() {
        return Web3j.build(new HttpService());
    }

    private BigInteger calculateGasPrice(Web3j web3j) throws IOException {
        return web3j.ethGasPrice().send().getGasPrice();
    }

    private Option<UUID> referenceFromInput(byte[] input) {
        return Try.of(() -> new String(hexStringToByteArray(new String(input, "UTF-8")), "UTF-8")).map(UUID::fromString).toOption();
    }

}
