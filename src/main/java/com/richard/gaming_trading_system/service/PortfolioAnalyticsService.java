package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.*;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import com.richard.gaming_trading_system.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioAnalyticsService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TradeRepository tradeRepository;

    public Map<Long, Integer> getMostTradedAssets() {
        List<Trade> allTrades = tradeRepository.findAll();
        return allTrades.stream()
                .collect(Collectors.groupingBy(
                        Trade::getAssetId,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<Long, BigDecimal> getHighestPortfolioValues() {
        return portfolioRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Portfolio::getUserId,
                        Portfolio::getTotalValue,
                        (p1, p2) -> p1,
                        () -> new TreeMap<>(Collections.reverseOrder())
                ));
    }

    public Map<Long, BigDecimal> getPortfolioPerformance(Long userId, LocalDateTime startTime) {
        List<Trade> userTrades = tradeRepository.findByUserId(userId).stream()
                .filter(trade -> trade.getTradeTimestamp().isAfter(startTime))
                .collect(Collectors.toList());

        Map<Long, BigDecimal> assetPerformance = new HashMap<>();
        for (Trade trade : userTrades) {
            BigDecimal currentValue = trade.getQuantity().multiply(trade.getPrice());
            assetPerformance.merge(trade.getAssetId(), currentValue, BigDecimal::add);
        }

        return assetPerformance;
    }

    public Map<Long, Integer> getTradingVolumeByAsset() {
        return tradeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Trade::getAssetId,
                        Collectors.collectingAndThen(
                                Collectors.summingInt(trade -> trade.getQuantity().intValue()),
                                Integer::valueOf
                        )
                ));
    }

    public Map<Long, BigDecimal> getAverageTradeSizeByUser() {
        return tradeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Trade::getUserId,
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(trade -> trade.getTotalAmount().doubleValue()),
                                avg -> BigDecimal.valueOf(avg)
                        )
                ));
    }
} 