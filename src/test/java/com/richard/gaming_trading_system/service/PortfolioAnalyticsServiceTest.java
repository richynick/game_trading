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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioAnalyticsServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private PortfolioAnalyticsService portfolioAnalyticsService;

    private User testUser;
    private Asset testAsset1;
    private Asset testAsset2;
    private Portfolio testPortfolio;
    private Trade testTrade1;
    private Trade testTrade2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test user
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUser");

        // Setup test assets
        testAsset1 = new Asset();
        testAsset1.setAssetId(1L);
        testAsset1.setSymbol("TEST1");
        testAsset1.setName("Test Asset 1");
        testAsset1.setCurrentPrice(new BigDecimal("100.00"));

        testAsset2 = new Asset();
        testAsset2.setAssetId(2L);
        testAsset2.setSymbol("TEST2");
        testAsset2.setName("Test Asset 2");
        testAsset2.setCurrentPrice(new BigDecimal("200.00"));

        // Setup test portfolio
        testPortfolio = new Portfolio();
        testPortfolio.setPortfolioId(1L);
        testPortfolio.setUserId(testUser.getUserId());
        testPortfolio.setName("Test Portfolio");

        // Setup test trades
        testTrade1 = new Trade();
        testTrade1.setTradeId(1L);
        testTrade1.setPortfolio(testPortfolio);
        testTrade1.setUserId(testUser.getUserId());
        testTrade1.setAssetId(testAsset1.getAssetId());
        testTrade1.setQuantity(new BigDecimal("10"));
        testTrade1.setPrice(new BigDecimal("100.00"));
        testTrade1.setTradeType(TradeType.BUY);
        testTrade1.setTimestamp(LocalDateTime.now());

        testTrade2 = new Trade();
        testTrade2.setTradeId(2L);
        testTrade2.setPortfolio(testPortfolio);
        testTrade2.setUserId(testUser.getUserId());
        testTrade2.setAssetId(testAsset2.getAssetId());
        testTrade2.setQuantity(new BigDecimal("5"));
        testTrade2.setPrice(new BigDecimal("200.00"));
        testTrade2.setTradeType(TradeType.SELL);
        testTrade2.setTimestamp(LocalDateTime.now());
    }

    @Test
    void getMostTradedAssets_Success() {
        // Create a list of trades with the same asset ID to test counting
        Trade trade1 = new Trade();
        trade1.setAssetId(testAsset1.getAssetId());
        trade1.setPortfolio(testPortfolio);
        trade1.setUserId(testUser.getUserId());
        trade1.setQuantity(new BigDecimal("10"));
        trade1.setPrice(new BigDecimal("100.00"));
        trade1.setTradeType(TradeType.BUY);
        trade1.setTimestamp(LocalDateTime.now());

        Trade trade2 = new Trade();
        trade2.setAssetId(testAsset1.getAssetId());
        trade2.setPortfolio(testPortfolio);
        trade2.setUserId(testUser.getUserId());
        trade2.setQuantity(new BigDecimal("5"));
        trade2.setPrice(new BigDecimal("100.00"));
        trade2.setTradeType(TradeType.BUY);
        trade2.setTimestamp(LocalDateTime.now());

        Trade trade3 = new Trade();
        trade3.setAssetId(testAsset2.getAssetId());
        trade3.setPortfolio(testPortfolio);
        trade3.setUserId(testUser.getUserId());
        trade3.setQuantity(new BigDecimal("5"));
        trade3.setPrice(new BigDecimal("200.00"));
        trade3.setTradeType(TradeType.SELL);
        trade3.setTimestamp(LocalDateTime.now());

        List<Trade> trades = Arrays.asList(trade1, trade2, trade3);
        when(tradeRepository.findAll()).thenReturn(trades);

        Map<Long, Integer> result = portfolioAnalyticsService.getMostTradedAssets();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(testAsset1.getAssetId())); // TEST1 traded twice
        assertEquals(1, result.get(testAsset2.getAssetId())); // TEST2 traded once
    }

    @Test
    void getHighestPortfolioValues_Success() {
        PortfolioAsset portfolioAsset1 = new PortfolioAsset();
        portfolioAsset1.setAsset(testAsset1);
        portfolioAsset1.setQuantity(new BigDecimal("10"));
        portfolioAsset1.setPrice(new BigDecimal("100.00"));

        PortfolioAsset portfolioAsset2 = new PortfolioAsset();
        portfolioAsset2.setAsset(testAsset2);
        portfolioAsset2.setQuantity(new BigDecimal("5"));
        portfolioAsset2.setPrice(new BigDecimal("200.00"));

        testPortfolio.getAssets().add(portfolioAsset1);
        testPortfolio.getAssets().add(portfolioAsset2);

        when(portfolioRepository.findAll()).thenReturn(Arrays.asList(testPortfolio));

        Map<Long, BigDecimal> result = portfolioAnalyticsService.getHighestPortfolioValues();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("2000.00"), result.get(testUser.getUserId())); // (10 * 100) + (5 * 200)
    }

    @Test
    void getPortfolioPerformance_Success() {
        List<Trade> trades = Arrays.asList(testTrade1, testTrade2);
        when(tradeRepository.findByUserId(testUser.getUserId())).thenReturn(trades);

        Map<Long, BigDecimal> result = portfolioAnalyticsService.getPortfolioPerformance(
            testUser.getUserId(),
            LocalDateTime.now().minusDays(1)
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("1000.00"), result.get(testAsset1.getAssetId())); // 10 * 100
        assertEquals(new BigDecimal("1000.00"), result.get(testAsset2.getAssetId())); // 5 * 200
    }

    @Test
    void getTradingVolumeByAsset_Success() {
        List<Trade> trades = Arrays.asList(testTrade1, testTrade1, testTrade2);
        when(tradeRepository.findAll()).thenReturn(trades);

        Map<Long, Integer> result = portfolioAnalyticsService.getTradingVolumeByAsset();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(20, result.get(testAsset1.getAssetId())); // 10 * 2 trades
        assertEquals(5, result.get(testAsset2.getAssetId())); // 5 * 1 trade
    }

} 
