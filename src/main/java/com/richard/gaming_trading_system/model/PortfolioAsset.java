package com.richard.gaming_trading_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class PortfolioAsset {
    @NotNull
    private Long portfolioId;

    @NotNull
    private Long assetId;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal averagePrice;

    private LocalDateTime lastTraded;

    private Asset asset;

    public PortfolioAsset() {}

    public PortfolioAsset(Long portfolioId, Long assetId, BigDecimal quantity, BigDecimal averagePrice) {
        this.portfolioId = portfolioId;
        this.assetId = assetId;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.lastTraded = LocalDateTime.now();
    }

    public BigDecimal getTotalValue() {
        return quantity.multiply(averagePrice);
    }

    // Getters and Setters
    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }

    public LocalDateTime getLastTraded() { return lastTraded; }
    public void setLastTraded(LocalDateTime lastTraded) { this.lastTraded = lastTraded; }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }

    public void setPrice(BigDecimal price) { this.averagePrice = price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioAsset that = (PortfolioAsset) o;
        return Objects.equals(portfolioId, that.portfolioId) && Objects.equals(assetId, that.assetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioId, assetId);
    }
}
