package com.richard.gaming_trading_system.controller;

import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.dto.TradeRequest;
import com.richard.gaming_trading_system.dto.UserStatsResponse;
import com.richard.gaming_trading_system.model.Portfolio;
import com.richard.gaming_trading_system.model.Trade;
import com.richard.gaming_trading_system.model.TradeType;
import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.service.PortfolioService;
import com.richard.gaming_trading_system.service.RankingService;
import com.richard.gaming_trading_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TradingController {

    @Autowired
    private UserService userService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private RankingService rankingService;

    // User Management Endpoints
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.createUser(username));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserStats(userId));
    }

    // Portfolio Management Endpoints
    @PostMapping("/portfolios")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody CreatePortfolioRequest request) {
        return ResponseEntity.ok(portfolioService.createPortfolio(request));
    }

    @GetMapping("/portfolios/{portfolioId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(portfolioService.getPortfolioById(portfolioId));
    }

    @GetMapping("/users/{userId}/portfolios")
    public ResponseEntity<List<Portfolio>> getUserPortfolios(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getPortfoliosByUserId(userId));
    }

    // Trading Endpoints
    @PostMapping("/trade")
    public ResponseEntity<Trade> executeTrade(@RequestBody TradeRequest request) {
        Trade trade = portfolioService.executeTrade(
            request.getPortfolioId(),
            request.getAssetId(),
            request.getQuantity(),
            request.getPrice(),
            request.getTradeType()
        );
        return ResponseEntity.ok(trade);
    }

    // Leaderboard Endpoints
    @GetMapping("/leaderboard")
    public ResponseEntity<List<User>> getLeaderboard() {
        return ResponseEntity.ok(rankingService.getLeaderboard());
    }

    @GetMapping("/leaderboard/top")
    public ResponseEntity<List<User>> getTopUsers(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(rankingService.getTopUsers(limit));
    }
} 