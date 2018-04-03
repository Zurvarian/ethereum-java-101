package ether.transaction.model;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.UUID;

@Value
public class TransactionReceive {

    @NonNull
    UUID walletId;

    @NonNull
    String address;

    @NonNull
    UUID transactionId;

    @NonNull
    UUID reference;

    @NonNull
    @Wither
    TransactionReceiveStatus status;

    public enum TransactionReceiveStatus {
        ACCEPTED, COMPLETED, UNKNOWN
    }
}
