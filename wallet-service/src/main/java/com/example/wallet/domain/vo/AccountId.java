package com.example.wallet.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class AccountId {
    private final UUID value;

    private AccountId(UUID value) {
        this.value = Objects.requireNonNull(value, "AccountId cannot be null");
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(UUID value) {
        return new AccountId(value);
    }

    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccountId walletId = (AccountId) o;
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
