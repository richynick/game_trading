package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreatePortfolioRequest;
import com.richard.gaming_trading_system.exception.AssetNotFoundException;
import com.richard.gaming_trading_system.exception.InsufficientAssetException;
import com.richard.gaming_trading_system.exception.PortfolioNotFoundException;
import com.richard.gaming_trading_system.exception.UserNotFoundException;
import com.richard.gaming_trading_system.model.*;
import com.richard.gaming_trading_system.repository.AssetRepository;
import com.richard.gaming_trading_system.repository.PortfolioRepository;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserService userService;

    @Mock
    private PortfolioValueService portfolioValueService;

    @InjectMocks
    private PortfolioService portfolioService;

    private User testUser;
    private Asset testAsset;
    private Portfolio testPortfolio;
    private CreatePortfolioRequest createPortfolioRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUser");
        testUser.setGemCount(2000);

        testAsset = new Asset();
        testAsset.setAssetId(1L);
        testAsset.setSymbol("TEST");
        testAsset.setName("Test Asset");
        testAsset.setCurrentPrice(new BigDecimal("100.00"));

        testPortfolio = new Portfolio();
        testPortfolio.setPortfolioId(1L);
        testPortfolio.setUserId(testUser.getUserId());
        testPortfolio.setName("Test Portfolio");

        createPortfolioRequest = new CreatePortfolioRequest();
        createPortfolioRequest.setUserId(testUser.getUserId());
        createPortfolioRequest.setName("Test Portfolio");
    }

    @Test
    void createPortfolio_Success() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(testPortfolio);

        Portfolio result = portfolioService.createPortfolio(createPortfolioRequest);

        assertNotNull(result);
        assertEquals(testPortfolio.getPortfolioId(), result.getPortfolioId());
        assertEquals(testPortfolio.getUserId(), result.getUserId());
        assertEquals(testPortfolio.getName(), result.getName());
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void createPortfolio_UserNotFound() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> portfolioService.createPortfolio(createPortfolioRequest));
        verify(portfolioRepository, never()).save(any(Portfolio.class));
    }

    @Test
    void getPortfolioById_Success() {
        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));

        Portfolio result = portfolioService.getPortfolioById(testPortfolio.getPortfolioId());

        assertNotNull(result);
        assertEquals(testPortfolio.getPortfolioId(), result.getPortfolioId());
    }

    @Test
    void getPortfolioById_NotFound() {
        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.empty());

        assertThrows(PortfolioNotFoundException.class, () -> portfolioService.getPortfolioById(testPortfolio.getPortfolioId()));
    }

    @Test
    void getPortfoliosByUserId_Success() {
        List<Portfolio> expectedPortfolios = Arrays.asList(testPortfolio);
        when(portfolioRepository.findByUserId(testUser.getUserId())).thenReturn(expectedPortfolios);

        List<Portfolio> result = portfolioService.getPortfoliosByUserId(testUser.getUserId());

        assertNotNull(result);
        assertEquals(expectedPortfolios.size(), result.size());
        assertEquals(expectedPortfolios.get(0).getPortfolioId(), result.get(0).getPortfolioId());
    }

    @Test
    void addAssetToPortfolio_Success() {
        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));
        when(assetRepository.findById(testAsset.getAssetId())).thenReturn(Optional.of(testAsset));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(testPortfolio);

        Portfolio result = portfolioService.addAssetToPortfolio(
            testPortfolio.getPortfolioId(),
            testAsset.getAssetId(),
            new BigDecimal("10"),
            new BigDecimal("100.00")
        );

        assertNotNull(result);
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void removeAssetFromPortfolio_Success() {
        PortfolioAsset portfolioAsset = new PortfolioAsset();
        portfolioAsset.setAsset(testAsset);
        portfolioAsset.setQuantity(new BigDecimal("20"));
        testPortfolio.getAssets().add(portfolioAsset);

        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(testPortfolio);

        Portfolio result = portfolioService.removeAssetFromPortfolio(
            testPortfolio.getPortfolioId(),
            testAsset.getAssetId(),
            new BigDecimal("10")
        );

        assertNotNull(result);
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void removeAssetFromPortfolio_InsufficientQuantity() {
        PortfolioAsset portfolioAsset = new PortfolioAsset();
        portfolioAsset.setAsset(testAsset);
        portfolioAsset.setQuantity(new BigDecimal("5"));
        testPortfolio.getAssets().add(portfolioAsset);

        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));

        assertThrows(InsufficientAssetException.class, () -> 
            portfolioService.removeAssetFromPortfolio(
                testPortfolio.getPortfolioId(),
                testAsset.getAssetId(),
                new BigDecimal("10")
            )
        );
    }

    @Test
    void executeTrade_Buy_Success() {
        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(assetRepository.findById(testAsset.getAssetId())).thenReturn(Optional.of(testAsset));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(testPortfolio);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userService.updateUserGems(anyLong(), anyInt())).thenReturn(testUser);
        doNothing().when(userService).incrementTradeCount(anyLong());

        Trade result = portfolioService.executeTrade(
            testPortfolio.getPortfolioId(),
            testAsset.getAssetId(),
            new BigDecimal("10"),
            new BigDecimal("100.00"),
            TradeType.BUY
        );

        assertNotNull(result);
        assertEquals(TradeType.BUY, result.getTradeType());
        assertEquals(new BigDecimal("10"), result.getQuantity());
        assertEquals(new BigDecimal("100.00"), result.getPrice());
        verify(userRepository).save(any(User.class));
        verify(userService).updateUserGems(testUser.getUserId(), -1000); // -1000 gems for buying 10 units at 100.00 each
        verify(userService).incrementTradeCount(testUser.getUserId());
    }

    @Test
    void executeTrade_Sell_Success() {
        PortfolioAsset portfolioAsset = new PortfolioAsset();
        portfolioAsset.setAsset(testAsset);
        portfolioAsset.setQuantity(new BigDecimal("20"));
        testPortfolio.getAssets().add(portfolioAsset);

        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.of(testUser));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(testPortfolio);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userService.updateUserGems(anyLong(), anyInt())).thenReturn(testUser);
        doNothing().when(userService).incrementTradeCount(anyLong());

        Trade result = portfolioService.executeTrade(
            testPortfolio.getPortfolioId(),
            testAsset.getAssetId(),
            new BigDecimal("10"),
            new BigDecimal("100.00"),
            TradeType.SELL
        );

        assertNotNull(result);
        assertEquals(TradeType.SELL, result.getTradeType());
        assertEquals(new BigDecimal("10"), result.getQuantity());
        assertEquals(new BigDecimal("100.00"), result.getPrice());
        verify(userRepository).save(any(User.class));
        verify(userService).updateUserGems(testUser.getUserId(), 1000); // +1000 gems for selling 10 units at 100.00 each
        verify(userService).incrementTradeCount(testUser.getUserId());
    }

    @Test
    void getPortfolioValue_Success() {
        PortfolioAsset portfolioAsset = new PortfolioAsset();
        portfolioAsset.setAsset(testAsset);
        portfolioAsset.setQuantity(new BigDecimal("10"));
        portfolioAsset.setPrice(new BigDecimal("100.00"));
        testPortfolio.getAssets().add(portfolioAsset);

        when(portfolioRepository.findById(testPortfolio.getPortfolioId())).thenReturn(Optional.of(testPortfolio));
        when(portfolioValueService.getPortfolioValue(testPortfolio.getPortfolioId())).thenReturn(new BigDecimal("1000.00"));

        BigDecimal value = portfolioValueService.getPortfolioValue(testPortfolio.getPortfolioId());

        assertNotNull(value);
        assertEquals(new BigDecimal("1000.00"), value);
    }
} 