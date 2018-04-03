package ether.transaction;

import ether.transaction.model.Transaction;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static io.vavr.collection.List.ofAll;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TransactionRepository {

    Map<UUID, Transaction> transactionsById = new ConcurrentHashMap<>();

    void save(@NonNull Transaction transaction) {
        transactionsById.put(transaction.getId(), transaction);
    }

    Option<Transaction> findById(@NonNull UUID transactionId) {
        return Option.of(transactionsById.get(transactionId));
    }

    List<Transaction> findByWalletId(@NonNull UUID walletId) {
        return ofAll(transactionsById.values()).filter(transaction -> transaction.getWalletId().equals(walletId));
    }

    Option<Transaction> findByReference(@NonNull UUID reference) {
        return ofAll(transactionsById.values()).filter(transaction -> transaction.getReference().equals(reference)).headOption();
    }

}
