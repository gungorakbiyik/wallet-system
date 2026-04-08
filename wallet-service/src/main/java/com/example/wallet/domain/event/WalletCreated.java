package com.example.wallet.domain.event;

import com.example.wallet.domain.vo.AccountId;
import com.example.wallet.domain.vo.Currency;
import com.example.wallet.domain.vo.WalletId;

public class WalletCreated extends DomainEvent {
    private final WalletId walletId;
    private final AccountId accountId;
    private final Currency currency;

    public WalletCreated(WalletId walletId, AccountId accountId, Currency currency) {
        super();
        this.walletId = walletId;
        this.accountId = accountId;
        this.currency = currency;
    }
    public WalletId getWalletId() {
        return walletId;
    }
    public AccountId getAccountId() {
        return accountId;
    }
    public Currency getCurrency() {
        return currency;
    }
}
