package ether.transaction.model;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CreateTransactionReceive {

    @NonNull
    UUID walletId;

    @NonNull
    UUID reference;

}
