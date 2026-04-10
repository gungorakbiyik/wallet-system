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
    private Long version;
    private final List<Transaction> transactions = new ArrayList<>();

    // Dışarıya publish edilmeyi bekleyen domain event'leri
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    // -----------------------------------------------------------------
    // Factory method — yeni Wallet oluşturma
    // Constructor'ı private tutuyoruz; nesne yaratmanın tek yolu bu metod
    // ve rehydration (aşağıda)
    // -----------------------------------------------------------------
    public static Wallet create(AccountId accountId, Currency currency) {

        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }

        if (currency == null) {
            throw new IllegalArgumentException("currency cannot be null");
        }

        var wallet = new Wallet();

        wallet.walletId = WalletId.generate();
        wallet.accountId = accountId;
        wallet.balance = Money.zero(currency);
        wallet.status = WalletStatus.ACTIVE;
        var event = new WalletCreated(
                wallet.walletId,
                wallet.accountId,
                wallet.balance.getCurrency()
        );
        wallet.domainEvents.add(event);
        return wallet;
    }

    /** Only for infrastructure layer — JPA reconstitution. */
    public static Wallet initialize(WalletId walletId,
                                    AccountId accountId,
                                    Money balance,
                                    WalletStatus status,
                                    Long version) {
        Wallet wallet = new Wallet();
        wallet.walletId = walletId;
        wallet.accountId = accountId;
        wallet.balance = balance;
        wallet.status = status;
        wallet.version = version;
        return wallet;
    }

    public void deposit(Money depositAmount, ReferenceId referenceId) {
        // wallet aktif mi?
        requireWalletActive();
        // depositAmount sıfırdan büyük mü
        requirePositiveAmount(depositAmount);
        // aynı reference id daha önce kullanılmış mı?
        requireNoDuplicate(referenceId);

        this.balance = this.balance.add(depositAmount);
        // MoneyDeposited
        MoneyDeposited event = new MoneyDeposited(
                this.walletId,
                depositAmount,
                referenceId
        );
        domainEvents.add(event);

    }

    public void withdraw(Money withdrawAmount, ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(withdrawAmount);
        requireNoDuplicate(referenceId);
        requireSufficientBalance(withdrawAmount);

        this.balance = this.balance.subtract(withdrawAmount);
        MoneyWithdrawn event = new MoneyWithdrawn(
                this.walletId,
                withdrawAmount,
                referenceId
        );
        domainEvents.add(event);
    }

    public void initiateTransfer(Money transferAmount,
                                 WalletId targetWalletId,
                                 ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(transferAmount);
        requireNoDuplicate(referenceId);
        requireSufficientBalance(transferAmount);

        this.balance = this.balance.subtract(transferAmount);
        TransferInitiated event = new TransferInitiated(
                this.walletId,
                targetWalletId,
                transferAmount,
                referenceId
        );
        domainEvents.add(event);
    }

    public void receiveTransfer(Money transferAmount,
                                WalletId sourceWalletId,
                                ReferenceId referenceId) {
        requireWalletActive();
        requirePositiveAmount(transferAmount);
        requireNoDuplicate(referenceId);

        this.balance = this.balance.add(transferAmount);
        TransferReceived event = new TransferReceived(
                sourceWalletId,
                this.walletId,
                transferAmount,
                referenceId
        );
        domainEvents.add(event);
    }

    public void block(String reason) {
        requireWalletActive();

        this.status = WalletStatus.BLOCKED;
        WalletBlocked event = new WalletBlocked(
                this.walletId,
                reason
        );
        domainEvents.add(event);
    }

    public void unblock(String reason) {
        requireWalletBlocked();

        this.status = WalletStatus.ACTIVE;
        WalletUnblocked event = new WalletUnblocked(
                this.walletId,
                reason
        );
        domainEvents.add(event);
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

    public Long getVersion() {
        return version;
    }
}
