package com.example.wallet.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class WalletId {
    private final UUID value;

    private WalletId(UUID value) {
        this.value = Objects.requireNonNull(value, "WalletId cannot be null");
    }

    public static WalletId generate() {
        return new WalletId(UUID.randomUUID());
    }

    public static WalletId of(UUID value) {
        return new WalletId(value);
    }

    public static WalletId of(String value) {
        return new WalletId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        WalletId walletId = (WalletId) o;
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
