package com.example.wallet.adapter.in.web;

import java.util.UUID;

public record CreateWalletRequest(UUID accountId, String currency) {
}
