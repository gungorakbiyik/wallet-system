package com.example.wallet.domain.port.in;

import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;

public interface TransferMoneyUseCase {
    Money transfer(WalletId sourceWalletId, WalletId targetWalletId, Money amount, ReferenceId referenceId);
}
