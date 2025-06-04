package com.richard.gaming_trading_system.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    public CreateUserRequest() {}

    public CreateUserRequest(String username) {
        this.username = username;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
