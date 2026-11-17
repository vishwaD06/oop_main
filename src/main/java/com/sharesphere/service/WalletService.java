package com.sharesphere.service;

import com.sharesphere.dto.response.WalletResponse;

import java.math.BigDecimal;

public interface WalletService {
    WalletResponse getWallet(Long userId);
    WalletResponse topUp(Long userId, BigDecimal amount);
    void holdEscrow(Long userId, BigDecimal amount);
    void refundEscrow(Long userId, BigDecimal amount);
    void transferFunds(Long fromUserId, Long toUserId, BigDecimal amount);
}
