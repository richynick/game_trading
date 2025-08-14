package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.UserStatsResponse;
import com.richard.gaming_trading_system.exception.UserNotFoundException;
import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RankingService rankingService;

    @Mock
    private PortfolioValueService portfolioValueService;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUser");
        testUser.setGemCount(100);
        testUser.setRank(1);
        testUser.setTotalTrades(10);
    }

    @Test
    void createUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(rankingService).updateUserRank(any(User.class));

        User result = userService.createUser("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository).save(any(User.class));
        verify(rankingService).updateUserRank(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.of(testUser));

        User result = userService.getUserById(testUser.getUserId());

        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(testUser.getUserId()));
    }

    @Test
    void getUserByUsername_Success() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(java.util.Optional.of(testUser));

        User result = userService.getUserByUsername(testUser.getUsername());

        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(testUser.getUsername()));
    }

    @Test
    void getUserStats_Success() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.of(testUser));
        when(portfolioValueService.getPortfolioValue(testUser.getUserId())).thenReturn(new BigDecimal("1000.00"));

        UserStatsResponse result = userService.getUserStats(testUser.getUserId());

        assertNotNull(result);
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getGemCount(), result.getGemCount());
        assertEquals(testUser.getRank(), result.getRank());
        assertEquals(testUser.getTotalTrades(), result.getTotalTrades());
        assertEquals(new BigDecimal("1000.00"), result.getPortfolioValue());
    }

    @Test
    void updateUserGems_Success() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(rankingService).updateUserRank(any(User.class));

        User result = userService.updateUserGems(testUser.getUserId(), 50);

        assertNotNull(result);
        assertEquals(150, result.getGemCount()); // 100 + 50
        verify(userRepository).save(any(User.class));
        verify(rankingService).updateUserRank(any(User.class));
    }

    @Test
    void incrementTradeCount_Success() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(java.util.Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.incrementTradeCount(testUser.getUserId());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(expectedUsers.size(), result.size());
        assertEquals(expectedUsers.get(0).getUserId(), result.get(0).getUserId());
    }
} 