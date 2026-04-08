package com.example.wallet.domain.model;

import com.example.wallet.domain.vo.*;

import java.time.Instant;

public class Transaction {
    private final TransactionId transactionId;
    private final TransactionType transactionType;
    private final Money amount;
    private final TransactionStatus transactionStatus;
    private final ReferenceId referenceId;
    private final Instant occurredAt;

    // Package-private constructor — sadece Wallet içinden oluşturulur
    Transaction(TransactionId transactionId,
                TransactionType transactionType,
                Money amount,
                TransactionStatus transactionStatus,
                ReferenceId referenceId,
                Instant occurredAt) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.referenceId = referenceId;
        this.occurredAt = occurredAt;
    }

    static Transaction create(TransactionType transactionType,
                              Money amount,
                              ReferenceId referenceId) {
        return new Transaction(
                TransactionId.generate(),
                transactionType,
                amount,
                TransactionStatus.COMPLETED,
                referenceId,
                Instant.now()
        );
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public ReferenceId getReferenceId() {
        return referenceId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
