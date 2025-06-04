package com.richard.gaming_trading_system.dto;

import com.richard.gaming_trading_system.model.TradeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TradeRequest {
    @NotNull
    private Long portfolioId;

    @NotNull
    private Long assetId;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    private TradeType tradeType;

    public TradeRequest() {}

    public TradeRequest(Long portfolioId, Long assetId, BigDecimal quantity, BigDecimal price, TradeType tradeType) {
        this.portfolioId = portfolioId;
        this.assetId = assetId;
        this.quantity = quantity;
        this.price = price;
        this.tradeType = tradeType;
    }

    // Getters and Setters
    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }
}
