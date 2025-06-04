package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreateUserRequest;
import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private RankingService rankingService;

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        User savedUser = userRepository.save(user);

        // Update rankings after creating user
        rankingService.updateAllRankings();

        return savedUser;
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
        BigDecimal portfolioValue = portfolioService.getTotalPortfolioValue(userId);

        return new UserStatsResponse(
                user.getUserId(),
                user.getUsername(),
                user.getGemCount(),
                user.getRank(),
                user.getTotalTrades(),
                portfolioValue
        );
    }

    public void updateGemCount(Long userId, int gems) {
        User user = getUserById(userId);
        user.addGems(gems);
        userRepository.save(user);

        // Update rankings after gem count change
        rankingService.updateAllRankings();
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
