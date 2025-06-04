package com.richard.gaming_trading_system.dto;

import com.richard.gaming_trading_system.model.TradeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TradeRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotNull(message = "Trade type is required")
    private TradeType tradeType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    public TradeRequest() {}

    public TradeRequest(Long userId, Long assetId, TradeType tradeType, BigDecimal quantity) {
        this.userId = userId;
        this.assetId = assetId;
        this.tradeType = tradeType;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
}
