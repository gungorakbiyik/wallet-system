package com.example.wallet.domain.vo;

import java.util.Objects;
import java.util.UUID;

public class ReferenceId {
    private final UUID value;

    private ReferenceId(UUID value) {
        this.value = Objects.requireNonNull(value, "ReferenceId cannot be null");
    }

    public static ReferenceId generate() {
        return new ReferenceId(UUID.randomUUID());
    }

    public static ReferenceId of(UUID value) {
        return new ReferenceId(value);
    }

    public static ReferenceId of(String value) {
        return new ReferenceId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ReferenceId walletId = (ReferenceId) o;
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
