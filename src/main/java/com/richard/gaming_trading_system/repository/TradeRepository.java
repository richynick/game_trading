package com.richard.gaming_trading_system.repository;

import com.richard.gaming_trading_system.model.Trade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TradeRepository {
    private final Map<Long, Trade> trades = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Trade save(Trade trade) {
        if (trade.getTradeId() == null) {
            trade.setTradeId(idGenerator.getAndIncrement());
        }
        trades.put(trade.getTradeId(), trade);
        return trade;
    }

    public Optional<Trade> findById(Long id) {
        return Optional.ofNullable(trades.get(id));
    }

    public List<Trade> findByUserId(Long userId) {
        return trades.values().stream()
                .filter(trade -> trade.getUserId().equals(userId))
                .sorted((t1, t2) -> t2.getTradeTimestamp().compareTo(t1.getTradeTimestamp()))
                .toList();
    }

    public List<Trade> findAll() {
        return new ArrayList<>(trades.values());
    }
}
