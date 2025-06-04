package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.model.Portfolio;
import com.richard.gaming_trading_system.repository.AssetRepository;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

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

    }

}
