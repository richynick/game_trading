package com.richard.gaming_trading_system.repository;

import com.richard.gaming_trading_system.model.Portfolio;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PortfolioRepository {
    private final Map<Long, Portfolio> portfolios = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Portfolio save(Portfolio portfolio) {
        if (portfolio.getPortfolioId() == null) {
            portfolio.setPortfolioId(idGenerator.getAndIncrement());
        }
        portfolios.put(portfolio.getPortfolioId(), portfolio);
        return portfolio;
    }

    public Optional<Portfolio> findById(Long id) {
        return Optional.ofNullable(portfolios.get(id));
    }

    public List<Portfolio> findByUserId(Long userId) {
        return portfolios.values().stream()
                .filter(portfolio -> portfolio.getUserId().equals(userId))
                .toList();
    }

    public List<Portfolio> findAll() {
        return new ArrayList<>(portfolios.values());
    }
}
