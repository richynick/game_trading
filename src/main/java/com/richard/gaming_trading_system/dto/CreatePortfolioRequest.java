package com.richard.gaming_trading_system.dto;

import jakarta.validation.constraints.NotNull;

public class CreatePortfolioRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    private String name;

    public CreatePortfolioRequest() {}

    public CreatePortfolioRequest(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
