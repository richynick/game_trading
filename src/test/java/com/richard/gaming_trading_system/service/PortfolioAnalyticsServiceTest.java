package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.*;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import com.richard.gaming_trading_system.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioAnalyticsServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private PortfolioAnalyticsService portfolioAnalyticsService;

    private User user1;
    private User user2;
    private Portfolio portfolio1;
    private Portfolio portfolio2;
    private Asset asset1;
    private Asset asset2;
    private List<Trade> trades;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        user1 = new User(1L, "user1");
        user2 = new User(2L, "user2");

        asset1 = new Asset();
        asset1.setAssetId(1L);
        asset1.setName("Asset 1");
        asset1.setCurrentPrice(new BigDecimal("10.00"));

        asset2 = new Asset();
        asset2.setAssetId(2L);
        asset2.setName("Asset 2");
        asset2.setCurrentPrice(new BigDecimal("20.00"));

        portfolio1 = new Portfolio();
        portfolio1.setPortfolioId(1L);
        portfolio1.setUserId(1L);
        portfolio1.setName("Portfolio 1");

        portfolio2 = new Portfolio();
        portfolio2.setPortfolioId(2L);
        portfolio2.setUserId(2L);
        portfolio2.setName("Portfolio 2");

        // Create test trades
        trades = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // User 1 trades
        Trade trade1 = new Trade();
        trade1.setTradeId(1L);
        trade1.setUserId(1L);
        trade1.setAssetId(1L);
        trade1.setQuantity(new BigDecimal("5"));
        trade1.setPrice(new BigDecimal("10.00"));
        trade1.setTradeTimestamp(now);
        trades.add(trade1);

        Trade trade2 = new Trade();
        trade2.setTradeId(2L);
        trade2.setUserId(1L);
        trade2.setAssetId(1L);
        trade2.setQuantity(new BigDecimal("3"));
        trade2.setPrice(new BigDecimal("10.00"));
        trade2.setTradeTimestamp(now);
        trades.add(trade2);

        // User 2 trades
        Trade trade3 = new Trade();
        trade3.setTradeId(3L);
        trade3.setUserId(2L);
        trade3.setAssetId(2L);
        trade3.setQuantity(new BigDecimal("2"));
        trade3.setPrice(new BigDecimal("20.00"));
        trade3.setTradeTimestamp(now);
        trades.add(trade3);

        // Setup portfolio assets
        PortfolioAsset pa1 = new PortfolioAsset();
        pa1.setAsset(asset1);
        pa1.setQuantity(new BigDecimal("8"));
        pa1.setPrice(new BigDecimal("10.00"));
        portfolio1.getAssets().add(pa1);

        PortfolioAsset pa2 = new PortfolioAsset();
        pa2.setAsset(asset2);
        pa2.setQuantity(new BigDecimal("2"));
        pa2.setPrice(new BigDecimal("20.00"));
        portfolio2.getAssets().add(pa2);

        // Mock repository responses
        when(tradeRepository.findAll()).thenReturn(trades);
        when(tradeRepository.findByUserId(1L)).thenReturn(trades.subList(0, 2));
        when(portfolioRepository.findAll()).thenReturn(Arrays.asList(portfolio1, portfolio2));
    }

    @Test
    void getMostTradedAssets() {
        Map<Long, Integer> result = portfolioAnalyticsService.getMostTradedAssets();
        
        assertEquals(2, result.size());
        assertEquals(2, result.get(1L)); // Asset 1 traded twice
        assertEquals(1, result.get(2L)); // Asset 2 traded once
    }

    @Test
    void getHighestPortfolioValues() {
        Map<Long, BigDecimal> result = portfolioAnalyticsService.getHighestPortfolioValues();
        
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("80.00"), result.get(1L)); // Portfolio 1: 8 * 10.00
        assertEquals(new BigDecimal("40.00"), result.get(2L)); // Portfolio 2: 2 * 20.00
    }

    @Test
    void getPortfolioPerformance() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        Map<Long, BigDecimal> result = portfolioAnalyticsService.getPortfolioPerformance(1L, startTime);
        
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("80.00"), result.get(1L)); // Total value of trades for asset 1
    }

    @Test
    void getTradingVolumeByAsset() {
        Map<Long, Integer> result = portfolioAnalyticsService.getTradingVolumeByAsset();
        
        assertEquals(2, result.size());
        assertEquals(8, result.get(1L)); // Asset 1: 5 + 3
        assertEquals(2, result.get(2L)); // Asset 2: 2
    }

    @Test
    void getAverageTradeSizeByUser() {
        Map<Long, BigDecimal> result = portfolioAnalyticsService.getAverageTradeSizeByUser();
        
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("40.00"), result.get(1L)); // User 1: (50 + 30) / 2
        assertEquals(new BigDecimal("40.00"), result.get(2L)); // User 2: 40 / 1
    }
} 