package com.example.wallet.domain.model;

import com.example.wallet.domain.event.DomainEvent;
import com.example.wallet.domain.event.MoneyDeposited;
import com.example.wallet.domain.event.WalletCreated;
import com.example.wallet.domain.vo.*;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    // --- State ---
    private WalletId walletId;
    private AccountId accountId;
    private Money balance;
    private WalletStatus status;
    private final List<Transaction> transactions = new ArrayList<>();

    // Dışarıya publish edilmeyi bekleyen domain event'leri
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // -----------------------------------------------------------------
    // Factory method — yeni Wallet oluşturma
    // Constructor'ı private tutuyoruz; nesne yaratmanın tek yolu bu metod
    // ve rehydration (aşağıda)
    // -----------------------------------------------------------------
    public static Wallet create(AccountId accountId, Currency currency) {
        var wallet = new Wallet();
        var event = new WalletCreated(
                WalletId.generate(),
                accountId,
                currency
        );
        wallet.apply(event);
        return wallet;
    }

    private void apply(DomainEvent event) {
        domainEvents.add(event);
        onEvent(event);
    }

    private void onEvent(DomainEvent event) {
        switch (event) {
            case WalletCreated e -> {
                this.walletId = e.getWalletId();
                this.accountId = e.getAccountId();
                this.balance = Money.zero(e.getCurrency());
                this.status = WalletStatus.ACTIVE;
            }
            case MoneyDeposited e -> {
                this.balance = this.balance.add(e.getAmount());
                this.transactions.add(Transaction.create(
                        TransactionType.DEPOSIT,
                        e.getAmount(),
                        e.getReferenceId()
                ));
            }
            default -> {
                throw new IllegalStateException("Unhandled event: " + event);
            }
        }
    }

    public void deposit(Money depositAmount, ReferenceId referenceId) {
        // wallet aktif mi?
        requireWalletActive();
        // depositAmount sıfırdan büyük mü
        requirePositiveAmount(depositAmount);
        // aynı reference id daha önce kullanılmış mı?
        requireNoDuplicate(referenceId);

        // MoneyDeposited
        MoneyDeposited event = new MoneyDeposited(
                this.walletId,
                depositAmount,
                referenceId
        );
        apply(event);

    }

    private void requireNoDuplicate(ReferenceId referenceId) {
        boolean exists = this.transactions.stream()
                .anyMatch(t -> t.getReferenceId().equals(referenceId));
        if (exists) {
            throw new IllegalArgumentException("Duplicate referenceId");
        }
    }

    private void requirePositiveAmount(Money amount) {
        if (amount.isLessThanOrEqualZero()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private void requireWalletActive() {
        if (status != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is not active");
        }
    }
}
