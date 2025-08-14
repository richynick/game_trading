package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RankingServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RankingService rankingService;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test users with different gem counts
        user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");
        user1.setGemCount(100);

        user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");
        user2.setGemCount(200);

        user3 = new User();
        user3.setUserId(3L);
        user3.setUsername("user3");
        user3.setGemCount(150);
    }

    @Test
    void updateUserRank_Success() {
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        rankingService.updateUserRank(user1);

        // Verify ranks are assigned correctly based on gem count
        assertEquals(3, user1.getRank()); // 100 gems
        assertEquals(1, user2.getRank()); // 200 gems
        assertEquals(2, user3.getRank()); // 150 gems

        verify(userRepository, times(3)).save(any(User.class));
    }

    @Test
    void updateUserRank_WithEqualGems() {
        // Set equal gem counts for two users
        user1.setGemCount(100);
        user2.setGemCount(100);
        user3.setGemCount(150);

        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        rankingService.updateUserRank(user1);

        // Verify ranks are assigned correctly with equal gem counts
        assertEquals(2, user1.getRank()); // 100 gems
        assertEquals(2, user2.getRank()); // 100 gems
        assertEquals(1, user3.getRank()); // 150 gems

        verify(userRepository, times(3)).save(any(User.class));
    }

    @Test
    void getTopUsers_Success() {
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> result = rankingService.getTopUsers(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user2.getUserId(), result.get(0).getUserId()); // Highest gems
        assertEquals(user3.getUserId(), result.get(1).getUserId()); // Second highest gems
    }

    @Test
    void getLeaderboard_Success() {
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> result = rankingService.getLeaderboard();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(user2.getUserId(), result.get(0).getUserId()); // Highest gems
        assertEquals(user3.getUserId(), result.get(1).getUserId()); // Second highest gems
        assertEquals(user1.getUserId(), result.get(2).getUserId()); // Lowest gems
    }

    @Test
    void getTopUsers_WithLimitGreaterThanUsers() {
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> result = rankingService.getTopUsers(5);

        assertNotNull(result);
        assertEquals(3, result.size()); // Should return all users even though limit is 5
    }
} 