package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreateUserRequest;
import com.richard.gaming_trading_system.dto.UserStatsResponse;
import com.richard.gaming_trading_system.exception.UserNotFoundException;
import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private RankingService rankingService;

    public User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user = userRepository.save(user);
        rankingService.updateUserRank(user);
        return user;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    public UserStatsResponse getUserStats(Long userId) {
        User user = getUserById(userId);
        BigDecimal portfolioValue = portfolioService.getPortfolioValue(userId);

        return new UserStatsResponse(
                user.getUserId(),
                user.getUsername(),
                user.getGemCount(),
                user.getRank(),
                user.getTotalTrades(),
                portfolioValue
        );
    }

    public User updateUserGems(Long userId, int gems) {
        User user = getUserById(userId);
        user.addGems(gems);
        user = userRepository.save(user);
        rankingService.updateUserRank(user);
        return user;
    }

    public void incrementTradeCount(Long userId) {
        User user = getUserById(userId);
        user.incrementTrades();
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
