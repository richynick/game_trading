package com.richard.gaming_trading_system.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    @NotNull
    private Long userId;

    @NotBlank
    private String username;

    private Integer gemCount = 0;
    private Integer rank = 0;
    private Integer totalTrades = 0;
    private Integer currentStreak = 0;
    private Integer longestStreak = 0;
    private LocalDateTime lastTradeTime;
    private LocalDateTime createdAt;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(Long userId, String username) {
        this();
        this.userId = userId;
        this.username = username;
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

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }

    public LocalDateTime getLastTradeTime() { return lastTradeTime; }
    public void setLastTradeTime(LocalDateTime lastTradeTime) { this.lastTradeTime = lastTradeTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void incrementTrades() {
        this.totalTrades++;
    }

    public void addGems(int gems) {
        this.gemCount += gems;
    }

    public void updateStreak() {
        LocalDateTime now = LocalDateTime.now();
        if (lastTradeTime != null && lastTradeTime.plusMinutes(30).isAfter(now)) {
            currentStreak++;
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
            }
        } else {
            currentStreak = 1;
        }
        lastTradeTime = now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
