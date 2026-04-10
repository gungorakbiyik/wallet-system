package com.example.wallet.infrastructure.persistence;

import com.example.wallet.domain.vo.WalletStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletJpaEntity {
    @Id
    @Column(name = "wallet_id")
    private UUID walletId;
    @Column(name = "account_id")
    private UUID accountId;
    @Column(name = "currency")
    private String currency;
    @Column(name = "balance")
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WalletStatus status;
    @Version
    @Column(name = "version")
    private Long version;
}
