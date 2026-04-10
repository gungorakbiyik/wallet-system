package com.example.wallet.infrastructure.persistence;

import com.example.wallet.domain.model.Wallet;
import com.example.wallet.domain.vo.AccountId;
import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.WalletId;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public WalletJpaEntity toJpaEntity(Wallet wallet) {
        WalletJpaEntity walletJpaEntity = new WalletJpaEntity();
        walletJpaEntity.setWalletId(wallet.getWalletId().getValue());
        walletJpaEntity.setAccountId(wallet.getAccountId().getValue());
        walletJpaEntity.setCurrency(wallet.getBalance().getCurrency());
        walletJpaEntity.setBalance(wallet.getBalance().getAmount());
        walletJpaEntity.setStatus(wallet.getStatus());
        walletJpaEntity.setVersion(wallet.getVersion());
        return walletJpaEntity;
    }

    public Wallet toDomain(WalletJpaEntity entity) {
        return Wallet.initialize(
                WalletId.of(entity.getWalletId()),
                AccountId.of(entity.getAccountId()),
                Money.of(entity.getBalance(), entity.getCurrency()),
                entity.getStatus(),
                entity.getVersion()
        );
    }

}
