package com.richard.gaming_trading_system.dto;

import java.math.BigDecimal;

public class UserStatsResponse {
    private Long userId;
    private String username;
    private Integer gemCount;
    private Integer rank;
    private Integer totalTrades;
    private BigDecimal portfolioValue;

    public UserStatsResponse() {}

    public UserStatsResponse(Long userId, String username, Integer gemCount, Integer rank, Integer totalTrades, BigDecimal portfolioValue) {
        this.userId = userId;
        this.username = username;
        this.gemCount = gemCount;
        this.rank = rank;
        this.totalTrades = totalTrades;
        this.portfolioValue = portfolioValue;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getGemCount() { return gemCount; }
    public void setGemCount(Integer gemCount) { this.gemCount = gemCount; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Integer getTotalTrades() { return totalTrades; }
    public void setTotalTrades(Integer totalTrades) { this.totalTrades = totalTrades; }

    public BigDecimal getPortfolioValue() { return portfolioValue; }
    public void setPortfolioValue(BigDecimal portfolioValue) { this.portfolioValue = portfolioValue; }
}
