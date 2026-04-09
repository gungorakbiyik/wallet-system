package com.example.wallet.domain.port.out;

import com.example.wallet.domain.event.DomainEvent;
import com.example.wallet.domain.vo.WalletId;

import java.util.List;

public interface EventStoreRepository {
    void save(DomainEvent event);
    List<DomainEvent> getEventsForWallet(WalletId walletId);
}
