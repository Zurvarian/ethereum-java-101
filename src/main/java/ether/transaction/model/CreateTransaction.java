package ether.transaction.model;

import lombok.NonNull;
import lombok.Value;

import java.math.BigInteger;
import java.util.UUID;

@Value
public class CreateTransaction {

    @NonNull
    UUID walletId;

    @NonNull
    String to;

    @NonNull
    BigInteger value;

    @NonNull
    UUID reference;

}
