package com.example.wallet.domain.port.in;

import com.example.wallet.domain.vo.WalletId;

public interface UnblockWalletUseCase {
    void unblock(WalletId walletId, String reason);
}
