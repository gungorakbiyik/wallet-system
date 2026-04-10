package com.example.wallet.adapter.in.web;

import com.example.wallet.domain.port.in.CreateWalletUseCase;
import com.example.wallet.domain.vo.AccountId;
import com.example.wallet.domain.vo.Currency;
import com.example.wallet.domain.vo.WalletId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final CreateWalletUseCase createWalletUseCase;

    public WalletController(CreateWalletUseCase createWalletUseCase) {
        this.createWalletUseCase = createWalletUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateWalletResponse> createWallet(@RequestBody CreateWalletRequest request) {
        WalletId walletId = createWalletUseCase.create(AccountId.of(request.accountId()), Currency.valueOf(request.currency()));
        return ResponseEntity
                .created(URI.create("/wallets/" + walletId.getValue()))
                .body(new CreateWalletResponse(walletId.getValue()));
    }
}
