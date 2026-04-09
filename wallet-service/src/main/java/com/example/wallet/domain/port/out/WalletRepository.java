package com.example.wallet.domain.port.out;

import com.example.wallet.domain.model.Wallet;
import com.example.wallet.domain.vo.WalletId;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findById(WalletId walletId);
    void save(Wallet wallet);
}
