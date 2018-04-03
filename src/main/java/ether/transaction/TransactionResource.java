package ether.transaction;

import ether.transaction.dto.CreateTransactionDto;
import ether.transaction.dto.SignTransactionDto;
import ether.transaction.dto.TransactionDto;
import ether.transaction.model.CreateTransaction;
import ether.transaction.model.Transaction;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.web3j.utils.Numeric.prependHexPrefix;

@RestController
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(path = "/transaction", produces = APPLICATION_JSON_VALUE)
public class TransactionResource {

    TransactionService transactionService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public TransactionDto createTransaction(@Valid @RequestBody CreateTransactionDto createTransactionDto) {
        return transactionToDto(transactionService.createTransaction(createTransactionDtoToModel(createTransactionDto)));
    }

    @PutMapping(path = "/{transactionId}/signature", consumes = APPLICATION_JSON_VALUE)
    public TransactionDto signTransaction(@PathVariable("transactionId") UUID transactionId, @Valid @RequestBody SignTransactionDto signTransactionDto) {
        return transactionToDto(transactionService.signTransaction(transactionId, signTransactionDto.getSignature()));
    }

    @PutMapping(path = "/{transactionId}/sign-with-wallet", consumes = APPLICATION_JSON_VALUE)
    public TransactionDto signTransactionWithWallet(@PathVariable("transactionId") UUID transactionId) {
        return transactionToDto(transactionService.signTransactionWithWallet(transactionId));
    }

    @PutMapping(path = "/{transactionId}/broadcast", consumes = APPLICATION_JSON_VALUE)
    public TransactionDto broadcastTransaction(@PathVariable("transactionId") UUID transactionId) {
        return transactionToDto(transactionService.broadcastTransaction(transactionId));
    }

    @GetMapping(path = "/{transactionId}")
    public TransactionDto findTransactionById(@PathVariable("transactionId") UUID transactionId) {
        return transactionToDto(transactionService.findTransactionById(transactionId));
    }

    @GetMapping
    public List<TransactionDto> findTransactionWithFilters(@RequestParam(name = "walletId", required = false) UUID walletId) {
        return transactionService.findTransactionsByWalletId(walletId).map(this::transactionToDto);
    }

    private CreateTransaction createTransactionDtoToModel(CreateTransactionDto createTransactionDto) {
        return new CreateTransaction(
                createTransactionDto.getWalletId(),
                prependHexPrefix(createTransactionDto.getTo()),
                createTransactionDto.getValue(),
                createTransactionDto.getReference()
        );
    }

    private TransactionDto transactionToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getWalletId(),
                transaction.getFrom(),
                transaction.getTo(),
                transaction.getValue(),
                transaction.getGasPrice(),
                transaction.getGas(),
                transaction.getReference(),
                transaction.getNonce(),
                transaction.getInput(),
                transaction.getSignature(),
                transaction.getHash(),
                transaction.getBlockHash(),
                transaction.getBlockNumber(),
                transaction.getTransactionIndex()
        );
    }

}
