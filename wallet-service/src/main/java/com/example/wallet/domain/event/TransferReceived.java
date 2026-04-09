package com.example.wallet.domain.event;

import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;

public class TransferReceived extends DomainEvent {
    private final WalletId sourceWalletId;
    private final WalletId targetWalletId;
    private final Money amount;
    private final ReferenceId referenceId;

    public TransferReceived(WalletId sourceWalletId,
                            WalletId targetWalletId,
                            Money amount,
                            ReferenceId referenceId) {
        super();
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.amount = amount;
        this.referenceId = referenceId;
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

    public ReferenceId getReferenceId() {
        return referenceId;
    }
}
