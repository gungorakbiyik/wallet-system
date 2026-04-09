package com.example.wallet.domain.event;

import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;

public class MoneyWithdrawn extends DomainEvent {
    private final WalletId walletId;
    private final Money amount;
    private final ReferenceId referenceId;

    public MoneyWithdrawn(WalletId walletId,
                          Money amount,
                          ReferenceId referenceId) {
        super();
        this.walletId = walletId;
        this.amount = amount;
        this.referenceId = referenceId;
    }

    public WalletId getWalletId() {
        return walletId;
    }
    public Money getAmount() {
        return amount;
    }
    public ReferenceId getReferenceId() {
        return referenceId;
    }
}
