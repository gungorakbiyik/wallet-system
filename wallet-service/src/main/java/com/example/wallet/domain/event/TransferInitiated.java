package com.example.wallet.domain.event;

import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.WalletId;

public class TransferInitiated extends DomainEvent {
    private final WalletId sourceWalletId;
    private final WalletId targetWalletId;
    private final Money amount;

    public TransferInitiated(WalletId sourceWalletId, WalletId targetWalletId, Money amount) {
        super();
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.amount = amount;
    }
    public WalletId getSourceWalletId() {
        return sourceWalletId;
    }
    public WalletId getTargetWalletId() {
        return targetWalletId;
    }
    public Money getAmount() {
        return amount;
    }
}
