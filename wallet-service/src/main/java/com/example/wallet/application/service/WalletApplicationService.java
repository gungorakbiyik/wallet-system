package com.example.wallet.application.service;

import com.example.wallet.domain.exception.WalletNotFoundException;
import com.example.wallet.domain.model.Wallet;
import com.example.wallet.domain.port.in.*;
import com.example.wallet.domain.port.out.EventStoreRepository;
import com.example.wallet.domain.port.out.WalletRepository;
import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class WalletApplicationService implements DepositMoneyUseCase, WithdrawMoneyUseCase,
        TransferMoneyUseCase, BlockWalletUseCase, UnblockWalletUseCase {

    private final WalletRepository walletRepository;
    private final EventStoreRepository eventStoreRepository;

    public WalletApplicationService(WalletRepository walletRepository,
                                    EventStoreRepository eventStoreRepository) {
        this.walletRepository = walletRepository;
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public Money deposit(WalletId walletId, Money amount, ReferenceId referenceId) {
        Wallet wallet = findWallet(walletId);
        wallet.deposit(amount, referenceId);
        walletRepository.save(wallet);
        eventStoreRepository.saveAll(wallet.getDomainEvents());
        wallet.clearDomainEvents();
        return wallet.getBalance();
    }

    @Override
    public Money withdraw(WalletId walletId, Money amount, ReferenceId referenceId) {
        Wallet wallet = findWallet(walletId);
        wallet.withdraw(amount, referenceId);
        walletRepository.save(wallet);
        eventStoreRepository.saveAll(wallet.getDomainEvents());
        wallet.clearDomainEvents();
        return wallet.getBalance();
    }

    @Override
    public Money transfer(WalletId sourceWalletId,
                          WalletId targetWalletId,
                          Money amount,
                          ReferenceId referenceId) {
        Wallet sourceWallet = findWallet(sourceWalletId);
        Wallet targetWallet = findWallet(targetWalletId);

        sourceWallet.initiateTransfer(amount, targetWalletId, referenceId);
        targetWallet.receiveTransfer(amount, sourceWalletId, referenceId);
        walletRepository.save(sourceWallet);
        walletRepository.save(targetWallet);
        eventStoreRepository.saveAll(sourceWallet.getDomainEvents());
        eventStoreRepository.saveAll(targetWallet.getDomainEvents());
        sourceWallet.clearDomainEvents();
        targetWallet.clearDomainEvents();
        return sourceWallet.getBalance();
    }

    @Override
    public void block(WalletId walletId, String reason) {
        Wallet wallet = findWallet(walletId);
        wallet.block(reason);
        walletRepository.save(wallet);
        eventStoreRepository.saveAll(wallet.getDomainEvents());
        wallet.clearDomainEvents();
    }

    @Override
    public void unblock(WalletId walletId, String reason) {
        Wallet wallet = findWallet(walletId);
        wallet.unblock(reason);
        walletRepository.save(wallet);
        eventStoreRepository.saveAll(wallet.getDomainEvents());
        wallet.clearDomainEvents();
    }

    private Wallet findWallet(WalletId walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
    }

}
