package com.richard.gaming_trading_system.controller;

import com.richard.gaming_trading_system.dto.CreateAssetRequest;
import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.dto.TradeRequest;
import com.richard.gaming_trading_system.dto.UserStatsResponse;
import com.richard.gaming_trading_system.model.Asset;
import com.richard.gaming_trading_system.model.Portfolio;
import com.richard.gaming_trading_system.model.Trade;
import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.service.AssetService;
import com.richard.gaming_trading_system.service.PortfolioAnalyticsService;
import com.richard.gaming_trading_system.service.PortfolioService;
import com.richard.gaming_trading_system.service.RankingService;
import com.richard.gaming_trading_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TradingController {

    private UserService userService;

    private PortfolioService portfolioService;

    private RankingService rankingService;

    private PortfolioAnalyticsService portfolioAnalyticsService;

    private AssetService assetService;

    @Autowired
    public TradingController(UserService userService, PortfolioService portfolioService, RankingService rankingService, PortfolioAnalyticsService portfolioAnalyticsService, AssetService assetService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.rankingService = rankingService;
        this.portfolioAnalyticsService = portfolioAnalyticsService;
        this.assetService = assetService;
    }

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

    // Analytics Endpoints
    @GetMapping("/analytics/most-traded")
    public ResponseEntity<Map<Long, Integer>> getMostTradedAssets() {
        return ResponseEntity.ok(portfolioAnalyticsService.getMostTradedAssets());
    }

    @GetMapping("/analytics/highest-portfolios")
    public ResponseEntity<Map<Long, BigDecimal>> getHighestPortfolioValues() {
        return ResponseEntity.ok(portfolioAnalyticsService.getHighestPortfolioValues());
    }

    @GetMapping("/analytics/portfolio-performance/{userId}")
    public ResponseEntity<Map<Long, BigDecimal>> getPortfolioPerformance(
            @PathVariable Long userId,
            @RequestParam(required = false) LocalDateTime startTime) {
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(7); // Default to last 7 days
        }
        return ResponseEntity.ok(portfolioAnalyticsService.getPortfolioPerformance(userId, startTime));
    }

    @GetMapping("/analytics/trading-volume")
    public ResponseEntity<Map<Long, Integer>> getTradingVolumeByAsset() {
        return ResponseEntity.ok(portfolioAnalyticsService.getTradingVolumeByAsset());
    }

    @GetMapping("/analytics/average-trade-size")
    public ResponseEntity<Map<Long, BigDecimal>> getAverageTradeSizeByUser() {
        return ResponseEntity.ok(portfolioAnalyticsService.getAverageTradeSizeByUser());
    }

    // Asset Management Endpoints
    @PostMapping("/assets")
    public ResponseEntity<Asset> createAsset(@RequestBody CreateAssetRequest request) {
        return ResponseEntity.ok(assetService.createAsset(request));
    }

    @GetMapping("/assets")
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/assets/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) {
        return ResponseEntity.ok(assetService.getAssetById(assetId));
    }

    @GetMapping("/assets/symbol/{symbol}")
    public ResponseEntity<Asset> getAssetBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(assetService.getAssetBySymbol(symbol));
    }
} 
