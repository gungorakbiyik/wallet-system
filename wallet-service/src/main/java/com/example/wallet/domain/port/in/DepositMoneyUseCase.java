package com.example.wallet.domain.port.in;

import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;

public interface DepositMoneyUseCase {
    Money deposit(WalletId walletId, Money amount, ReferenceId referenceId);
}
