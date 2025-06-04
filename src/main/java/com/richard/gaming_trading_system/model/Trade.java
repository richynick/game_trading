package com.richard.gaming_trading_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Trade {
    @NotNull
    private Long tradeId;

    @NotNull
    private Long userId;

    @NotNull
    private Long assetId;

    @NotNull
    private TradeType tradeType;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Positive
    private BigDecimal price;

    private Integer gemsAwarded = 1; // Default 1 gem per trade
    private LocalDateTime tradeTimestamp;

    private Portfolio portfolio;

    public Trade() {
        this.tradeTimestamp = LocalDateTime.now();
    }

    public Trade(Long tradeId, Long userId, Long assetId, TradeType tradeType,
                 BigDecimal quantity, BigDecimal price) {
        this();
        this.tradeId = tradeId;
        this.userId = userId;
        this.assetId = assetId;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return quantity.multiply(price);
    }

    // Getters and Setters
    public Long getTradeId() { return tradeId; }
    public void setTradeId(Long tradeId) { this.tradeId = tradeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getGemsAwarded() { return gemsAwarded; }
    public void setGemsAwarded(Integer gemsAwarded) { this.gemsAwarded = gemsAwarded; }

    public LocalDateTime getTradeTimestamp() { return tradeTimestamp; }
    public void setTradeTimestamp(LocalDateTime tradeTimestamp) { this.tradeTimestamp = tradeTimestamp; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }

    public void setTimestamp(LocalDateTime timestamp) { this.tradeTimestamp = timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(tradeId, trade.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId);
    }
}
