package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.exception.AssetNotFoundException;
import com.richard.gaming_trading_system.exception.InsufficientAssetException;
import com.richard.gaming_trading_system.exception.InsufficientFundsException;
import com.richard.gaming_trading_system.exception.PortfolioNotFoundException;
import com.richard.gaming_trading_system.exception.UserNotFoundException;
import com.richard.gaming_trading_system.model.*;
import com.richard.gaming_trading_system.repository.AssetRepository;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final UserService userService;

    @Autowired
    public PortfolioService(
            PortfolioRepository portfolioRepository,
            UserRepository userRepository,
            AssetRepository assetRepository,
            UserService userService) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.userService = userService;
    }

    public Portfolio createPortfolio(CreatePortfolioRequest request) {
        // Verify user exists
        if (!userRepository.findById(request.getUserId()).isPresent()) {
            throw new UserNotFoundException("User not found: " + request.getUserId());
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(request.getUserId());
        portfolio.setName(request.getName() != null ? request.getName() : "Default Portfolio");

        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolioById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found: " + portfolioId));
    }

    public List<Portfolio> getPortfoliosByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Portfolio addAssetToPortfolio(Long portfolioId, Long assetId, BigDecimal quantity, BigDecimal price) {
        Portfolio portfolio = getPortfolioById(portfolioId);
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));

        Optional<PortfolioAsset> existingAsset = portfolio.getAssets().stream()
                .filter(pa -> pa.getAsset().getAssetId().equals(assetId))
                .findFirst();

        if (existingAsset.isPresent()) {
            PortfolioAsset portfolioAsset = existingAsset.get();
            portfolioAsset.setQuantity(portfolioAsset.getQuantity().add(quantity));
            portfolioAsset.setPrice(price);
        } else {
            PortfolioAsset portfolioAsset = new PortfolioAsset();
            portfolioAsset.setAsset(asset);
            portfolioAsset.setQuantity(quantity);
            portfolioAsset.setPrice(price);
            portfolio.getAssets().add(portfolioAsset);
        }

        return portfolioRepository.save(portfolio);
    }

    public Portfolio removeAssetFromPortfolio(Long portfolioId, Long assetId, BigDecimal quantity) {
        Portfolio portfolio = getPortfolioById(portfolioId);
        
        PortfolioAsset portfolioAsset = portfolio.getAssets().stream()
                .filter(pa -> pa.getAsset().getAssetId().equals(assetId))
                .findFirst()
                .orElseThrow(() -> new AssetNotFoundException("Asset not found in portfolio: " + assetId));

        if (portfolioAsset.getQuantity().compareTo(quantity) < 0) {
            throw new InsufficientAssetException("Insufficient asset quantity in portfolio");
        }

        portfolioAsset.setQuantity(portfolioAsset.getQuantity().subtract(quantity));
        
        if (portfolioAsset.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            portfolio.getAssets().remove(portfolioAsset);
        }

        return portfolioRepository.save(portfolio);
    }

    public Trade executeTrade(Long portfolioId, Long assetId, BigDecimal quantity, BigDecimal price, TradeType tradeType) {
        Portfolio portfolio = getPortfolioById(portfolioId);
        User user = userRepository.findById(portfolio.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Calculate total cost of the trade
        BigDecimal totalCost = quantity.multiply(price);

        // For BUY trades, check if user has enough gems
        if (tradeType == TradeType.BUY) {
            if (user.getGemCount() < totalCost.intValue()) {
                throw new InsufficientFundsException(
                    String.format("Insufficient funds. Required: %d gems, Available: %d gems",
                        totalCost.intValue(), user.getGemCount())
                );
            }
            // Deduct gems for the purchase using UserService
            userService.updateUserGems(user.getUserId(), -totalCost.intValue());
        }

        // Execute trade based on type
        if (tradeType == TradeType.BUY) {
            portfolio = addAssetToPortfolio(portfolioId, assetId, quantity, price);
        } else {
            portfolio = removeAssetFromPortfolio(portfolioId, assetId, quantity);
            // Add gems for the sale using UserService
            userService.updateUserGems(user.getUserId(), totalCost.intValue());
        }

        // Create trade record
        Trade trade = new Trade();
        trade.setPortfolio(portfolio);
        trade.setAssetId(assetId);
        trade.setQuantity(quantity);
        trade.setPrice(price);
        trade.setTradeType(tradeType);
        trade.setTimestamp(LocalDateTime.now());
        trade.setUserId(user.getUserId());

        // Update user stats and award gems
        userService.incrementTradeCount(user.getUserId());
        awardGemsForTrade(user);

        userRepository.save(user);
        return trade;
    }

    private void awardGemsForTrade(User user) {
        // Base gem for trade
        int gemsToAward = 1;

        // Update streak and award streak bonus
        user.updateStreak();
        int currentStreak = user.getCurrentStreak();
        if (currentStreak >= 3) {
            gemsToAward += currentStreak; // Award additional gems equal to streak length
        }

        // Bonus gems for milestones
        int totalTrades = user.getTotalTrades();
        if (totalTrades % 10 == 0) {
            gemsToAward += 10; // 10 trades milestone
        } else if (totalTrades % 5 == 0) {
            gemsToAward += 5;  // 5 trades milestone
        }

        // Award gems using UserService
        userService.updateUserGems(user.getUserId(), gemsToAward);
    }
}
