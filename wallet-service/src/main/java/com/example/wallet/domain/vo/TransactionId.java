package com.example.wallet.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class TransactionId {
    private final UUID value;

    private TransactionId(UUID value) {
        this.value = Objects.requireNonNull(value, "TransactionId cannot be null");
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID());
    }

    public static TransactionId of(UUID value) {
        return new TransactionId(value);
    }

    public static TransactionId of(String value) {
        return new TransactionId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TransactionId walletId = (TransactionId) o;
        return value.equals(walletId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
