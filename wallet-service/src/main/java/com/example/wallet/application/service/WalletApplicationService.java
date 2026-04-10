package com.example.wallet.application.service;

import com.example.wallet.domain.event.DomainEvent;
import com.example.wallet.domain.exception.WalletNotFoundException;
import com.example.wallet.domain.model.Wallet;
import com.example.wallet.domain.port.in.*;
import com.example.wallet.domain.port.out.WalletRepository;
import com.example.wallet.domain.vo.Money;
import com.example.wallet.domain.vo.ReferenceId;
import com.example.wallet.domain.vo.WalletId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WalletApplicationService implements DepositMoneyUseCase, WithdrawMoneyUseCase,
        TransferMoneyUseCase, BlockWalletUseCase, UnblockWalletUseCase {

    private final WalletRepository walletRepository;

    public WalletApplicationService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Money deposit(WalletId walletId, Money amount, ReferenceId referenceId) {
        Wallet wallet = findWallet(walletId);
        wallet.deposit(amount, referenceId);
        walletRepository.save(wallet);
        wallet.clearDomainEvents();
        return wallet.getBalance();
    }

    @Override
    public Money withdraw(WalletId walletId, Money amount, ReferenceId referenceId) {
        Wallet wallet = findWallet(walletId);
        wallet.withdraw(amount, referenceId);
        walletRepository.save(wallet);
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
        sourceWallet.clearDomainEvents();
        targetWallet.clearDomainEvents();
        return sourceWallet.getBalance();
    }

    @Override
    public void block(WalletId walletId, String reason) {
        Wallet wallet = findWallet(walletId);
        wallet.block(reason);
        walletRepository.save(wallet);
        wallet.clearDomainEvents();
    }

    @Override
    public void unblock(WalletId walletId, String reason) {
        Wallet wallet = findWallet(walletId);
        wallet.unblock(reason);
        walletRepository.save(wallet);
        wallet.clearDomainEvents();
    }

    private Wallet findWallet(WalletId walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
        return wallet;
    }

}
