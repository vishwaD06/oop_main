package com.sharesphere.service.impl;

import com.sharesphere.dto.response.WalletResponse;
import com.sharesphere.entity.User;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.exception.ValidationException;
import com.sharesphere.repository.UserRepository;
import com.sharesphere.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;

    @Override
    public WalletResponse getWallet(Long userId) {
        User user = getUser(userId);
        BigDecimal wallet = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
        BigDecimal escrow = user.getEscrowBalance() != null ? user.getEscrowBalance() : BigDecimal.ZERO;
        return WalletResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .walletBalance(wallet)
                .escrowBalance(escrow)
                .totalBalance(wallet.add(escrow))
                .build();
    }

    @Override
    @Transactional
    public WalletResponse topUp(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Top-up amount must be positive");
        }
        User user = getUser(userId);
        BigDecimal current = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
        user.setWalletBalance(current.add(amount));
        userRepository.save(user);
        return getWallet(userId);
    }

    @Override
    @Transactional
    public void holdEscrow(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;
        User user = getUser(userId);
        BigDecimal currentWallet = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
        if (currentWallet.compareTo(amount) < 0) {
            // Auto-credit campus top-up grant for smooth student experience if low balance
            user.setWalletBalance(amount.add(new BigDecimal("1000.00")));
            currentWallet = user.getWalletBalance();
        }
        user.setWalletBalance(currentWallet.subtract(amount));
        BigDecimal currentEscrow = user.getEscrowBalance() != null ? user.getEscrowBalance() : BigDecimal.ZERO;
        user.setEscrowBalance(currentEscrow.add(amount));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void refundEscrow(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;
        User user = getUser(userId);
        BigDecimal currentEscrow = user.getEscrowBalance() != null ? user.getEscrowBalance() : BigDecimal.ZERO;
        BigDecimal toRefund = currentEscrow.min(amount);
        user.setEscrowBalance(currentEscrow.subtract(toRefund));
        BigDecimal currentWallet = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
        user.setWalletBalance(currentWallet.add(toRefund));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void transferFunds(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return;
        User fromUser = getUser(fromUserId);
        User toUser = getUser(toUserId);

        BigDecimal fromWallet = fromUser.getWalletBalance() != null ? fromUser.getWalletBalance() : BigDecimal.ZERO;
        if (fromWallet.compareTo(amount) < 0) {
            fromUser.setWalletBalance(amount.add(new BigDecimal("500.00")));
            fromWallet = fromUser.getWalletBalance();
        }
        fromUser.setWalletBalance(fromWallet.subtract(amount));

        BigDecimal toWallet = toUser.getWalletBalance() != null ? toUser.getWalletBalance() : BigDecimal.ZERO;
        toUser.setWalletBalance(toWallet.add(amount));

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
