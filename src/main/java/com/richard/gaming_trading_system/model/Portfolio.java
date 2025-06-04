package com.richard.gaming_trading_system.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Portfolio {

    @NotNull
    private Long portfolioId;

    @NotNull
    private Long userId;

    private String name;
    private List<PortfolioAsset> assets = new ArrayList<>();
    private LocalDateTime createdAt;

    public Portfolio() {
        this.createdAt = LocalDateTime.now();
    }

    public Portfolio(Long portfolioId, Long userId, String name) {
        this();
        this.portfolioId = portfolioId;
        this.userId = userId;
        this.name = name;
    }

    public BigDecimal getTotalValue() {
        return assets.stream()
                .map(PortfolioAsset::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PortfolioAsset> getAssets() { return assets; }
    public void setAssets(List<PortfolioAsset> assets) { this.assets = assets; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(portfolioId, portfolio.portfolioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioId);
    }
}
