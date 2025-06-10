package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.exception.PortfolioNotFoundException;
import com.richard.gaming_trading_system.model.Portfolio;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PortfolioValueService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioValueService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public BigDecimal getPortfolioValue(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found: " + portfolioId));
        return portfolio.getTotalValue();
    }
} 