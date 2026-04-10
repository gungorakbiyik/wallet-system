package com.example.wallet.domain.port.in;

import com.example.wallet.domain.vo.AccountId;
import com.example.wallet.domain.vo.Currency;
import com.example.wallet.domain.vo.WalletId;

public interface CreateWalletUseCase {
    WalletId create(AccountId accountId, Currency currency);
}
