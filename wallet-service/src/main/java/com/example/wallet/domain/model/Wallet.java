package com.example.wallet.domain.model;

import com.example.wallet.domain.event.*;
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
            case MoneyWithdrawn e -> {
                this.balance = this.balance.subtract(e.getAmount());
                this.transactions.add(Transaction.create(
                        TransactionType.WITHDRAWAL,
                        e.getAmount(),
                        e.getReferenceId()
                ));
            }
            case TransferInitiated e -> {
                this.balance = this.balance.subtract(e.getAmount());
                this.transactions.add(Transaction.create(
                        TransactionType.TRANSFER_OUT,
                        e.getAmount(),
                        e.getReferenceId()
                ));
            }
            case TransferReceived e -> {
                this.balance = this.balance.add(e.getAmount());
                this.transactions.add(Transaction.create(
                        TransactionType.TRANSFER_IN,
                        e.getAmount(),
                        e.getReferenceId()
                ));
            }
            case WalletBlocked e -> {
                this.status = WalletStatus.BLOCKED;

            }
            case WalletUnblocked e -> {
                this.status = WalletStatus.ACTIVE;
            }
            default -> {
                throw new IllegalStateException("Unhandled event: " + event);
            }
        }
    }

    public void rehydrate(List<DomainEvent> events) {
        events.forEach(this::onEvent);
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

    public void withdraw(Money withdrawAmount, ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(withdrawAmount);
        requireNoDuplicate(referenceId);
        requireSufficientBalance(withdrawAmount);
        MoneyWithdrawn event = new MoneyWithdrawn(
                this.walletId,
                withdrawAmount,
                referenceId
        );
        apply(event);
    }

    public void initiateTransfer(Money transferAmount,
                                 WalletId targetWalletId,
                                 ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(transferAmount);
        requireNoDuplicate(referenceId);
        requireSufficientBalance(transferAmount);

        TransferInitiated event = new TransferInitiated(
                this.walletId,
                targetWalletId,
                transferAmount,
                referenceId
        );
        apply(event);
    }

    public void receiveTransfer(Money transferAmount,
                                WalletId sourceWalletId,
                                ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(transferAmount);
        requireNoDuplicate(referenceId);

        TransferReceived event = new TransferReceived(
                sourceWalletId,
                this.walletId,
                transferAmount,
                referenceId
        );
        apply(event);
    }

    public void block(String reason) {
        requireWalletActive();

        WalletBlocked event = new WalletBlocked(
                this.walletId,
                reason
        );
        apply(event);
    }

    public void unblock(String reason) {
        requireWalletBlocked();

        WalletUnblocked event = new WalletUnblocked(
                this.walletId,
                reason
        );
        apply(event);
    }

    private void requireSufficientBalance(Money withdrawAmount) {
        if (this.balance.isLessThan(withdrawAmount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
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

    private void requireWalletBlocked() {
        if (status != WalletStatus.BLOCKED) {
            throw new IllegalStateException("Wallet is not blocked");
        }
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public WalletId getWalletId() {
        return walletId;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public Money getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
}
