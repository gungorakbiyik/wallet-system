package com.example.wallet.domain.event;

import com.example.wallet.domain.vo.WalletId;

public class WalletUnblocked extends DomainEvent {
    private final WalletId walletId;
    private final String reason;

    public WalletUnblocked(WalletId walletId, String reason) {
        super();
        this.walletId = walletId;
        this.reason = reason;
    }

    public WalletId getWalletId() {
        return walletId;
    }
    public String getReason() {
        return reason;
    }
}
