package com.example.wallet.infrastructure.persistence;

import com.example.wallet.domain.model.Wallet;
import com.example.wallet.domain.port.out.WalletRepository;
import com.example.wallet.domain.vo.WalletId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WalletJpaAdapter implements WalletRepository {
    private final WalletJpaRepository walletJpaRepository;
    private final WalletMapper walletMapper;


    public WalletJpaAdapter(WalletJpaRepository walletJpaRepository, WalletMapper walletMapper) {
        this.walletJpaRepository = walletJpaRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    public Optional<Wallet> findById(WalletId walletId) {
        return walletJpaRepository.findById(walletId.getValue())
                .map(walletMapper::toDomain);
    }

    @Override
    public void save(Wallet wallet) {
        walletJpaRepository.save(walletMapper.toJpaEntity(wallet));
    }
}
