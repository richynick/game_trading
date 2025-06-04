package com.richard.gaming_trading_system.controller;

import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.dto.TradeRequest;
import com.richard.gaming_trading_system.dto.UserStatsResponse;
import com.richard.gaming_trading_system.model.*;
import com.richard.gaming_trading_system.service.PortfolioAnalyticsService;
import com.richard.gaming_trading_system.service.PortfolioService;
import com.richard.gaming_trading_system.service.RankingService;
import com.richard.gaming_trading_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TradingControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PortfolioService portfolioService;

    @Mock
    private RankingService rankingService;

    @Mock
    private PortfolioAnalyticsService portfolioAnalyticsService;

    @InjectMocks
    private TradingController tradingController;

    private User user;
    private Portfolio portfolio;
    private Trade trade;
    private Map<Long, Integer> mostTradedAssets;
    private Map<Long, BigDecimal> highestPortfolios;
    private Map<Long, BigDecimal> portfolioPerformance;
    private Map<Long, Integer> tradingVolume;
    private Map<Long, BigDecimal> averageTradeSize;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test data
        user = new User(1L, "testUser");
        user.setGemCount(100);
        user.setRank(1);
        user.setTotalTrades(10);

        portfolio = new Portfolio();
        portfolio.setPortfolioId(1L);
        portfolio.setUserId(1L);
        portfolio.setName("Test Portfolio");

        trade = new Trade();
        trade.setTradeId(1L);
        trade.setUserId(1L);
        trade.setAssetId(1L);
        trade.setQuantity(new BigDecimal("5"));
        trade.setPrice(new BigDecimal("10.00"));
        trade.setTradeTimestamp(LocalDateTime.now());

        // Setup analytics data
        mostTradedAssets = new HashMap<>();
        mostTradedAssets.put(1L, 5);
        mostTradedAssets.put(2L, 3);

        highestPortfolios = new TreeMap<>(Collections.reverseOrder());
        highestPortfolios.put(1L, new BigDecimal("1000.00"));
        highestPortfolios.put(2L, new BigDecimal("500.00"));

        portfolioPerformance = new HashMap<>();
        portfolioPerformance.put(1L, new BigDecimal("100.00"));
        portfolioPerformance.put(2L, new BigDecimal("50.00"));

        tradingVolume = new HashMap<>();
        tradingVolume.put(1L, 100);
        tradingVolume.put(2L, 50);

        averageTradeSize = new HashMap<>();
        averageTradeSize.put(1L, new BigDecimal("100.00"));
        averageTradeSize.put(2L, new BigDecimal("50.00"));

        // Mock service responses
        when(userService.createUser(anyString())).thenReturn(user);
        when(userService.getUserStats(anyLong())).thenReturn(new UserStatsResponse(user));
        when(portfolioService.createPortfolio(any())).thenReturn(portfolio);
        when(portfolioService.getPortfolioById(anyLong())).thenReturn(portfolio);
        when(portfolioService.getPortfoliosByUserId(anyLong())).thenReturn(Collections.singletonList(portfolio));
        when(portfolioService.executeTrade(anyLong(), anyLong(), any(), any(), any())).thenReturn(trade);
        when(rankingService.getLeaderboard()).thenReturn(Collections.singletonList(user));
        when(rankingService.getTopUsers(anyInt())).thenReturn(Collections.singletonList(user));

        // Mock analytics service responses
        when(portfolioAnalyticsService.getMostTradedAssets()).thenReturn(mostTradedAssets);
        when(portfolioAnalyticsService.getHighestPortfolioValues()).thenReturn(highestPortfolios);
        when(portfolioAnalyticsService.getPortfolioPerformance(anyLong(), any())).thenReturn(portfolioPerformance);
        when(portfolioAnalyticsService.getTradingVolumeByAsset()).thenReturn(tradingVolume);
        when(portfolioAnalyticsService.getAverageTradeSizeByUser()).thenReturn(averageTradeSize);
    }

    @Test
    void getMostTradedAssets() {
        ResponseEntity<Map<Long, Integer>> response = tradingController.getMostTradedAssets();
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(mostTradedAssets, response.getBody());
        verify(portfolioAnalyticsService).getMostTradedAssets();
    }

    @Test
    void getHighestPortfolioValues() {
        ResponseEntity<Map<Long, BigDecimal>> response = tradingController.getHighestPortfolioValues();
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(highestPortfolios, response.getBody());
        verify(portfolioAnalyticsService).getHighestPortfolioValues();
    }

    @Test
    void getPortfolioPerformance() {
        ResponseEntity<Map<Long, BigDecimal>> response = tradingController.getPortfolioPerformance(1L, null);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(portfolioPerformance, response.getBody());
        verify(portfolioAnalyticsService).getPortfolioPerformance(eq(1L), any());
    }

    @Test
    void getTradingVolumeByAsset() {
        ResponseEntity<Map<Long, Integer>> response = tradingController.getTradingVolumeByAsset();
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(tradingVolume, response.getBody());
        verify(portfolioAnalyticsService).getTradingVolumeByAsset();
    }

    @Test
    void getAverageTradeSizeByUser() {
        ResponseEntity<Map<Long, BigDecimal>> response = tradingController.getAverageTradeSizeByUser();
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(averageTradeSize, response.getBody());
        verify(portfolioAnalyticsService).getAverageTradeSizeByUser();
    }

    // Test existing endpoints for completeness
    @Test
    void createUser() {
        ResponseEntity<User> response = tradingController.createUser("testUser");
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(user, response.getBody());
        verify(userService).createUser("testUser");
    }

    @Test
    void getUserStats() {
        ResponseEntity<UserStatsResponse> response = tradingController.getUserStats(1L);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        verify(userService).getUserStats(1L);
    }

    @Test
    void createPortfolio() {
        CreatePortfolioRequest request = new CreatePortfolioRequest();
        request.setUserId(1L);
        request.setName("Test Portfolio");
        
        ResponseEntity<Portfolio> response = tradingController.createPortfolio(request);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(portfolio, response.getBody());
        verify(portfolioService).createPortfolio(request);
    }

    @Test
    void executeTrade() {
        TradeRequest request = new TradeRequest();
        request.setPortfolioId(1L);
        request.setAssetId(1L);
        request.setQuantity(new BigDecimal("5"));
        request.setPrice(new BigDecimal("10.00"));
        request.setTradeType(TradeType.BUY);
        
        ResponseEntity<Trade> response = tradingController.executeTrade(request);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(trade, response.getBody());
        verify(portfolioService).executeTrade(
            request.getPortfolioId(),
            request.getAssetId(),
            request.getQuantity(),
            request.getPrice(),
            request.getTradeType()
        );
    }
} 