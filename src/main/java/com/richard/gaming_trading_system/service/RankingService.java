package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.User;
import com.richard.gaming_trading_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private UserRepository userRepository;

    public void updateUserRank(User user) {
        List<User> allUsers = userRepository.findAll();
        Map<Integer, List<User>> usersByGemCount = allUsers.stream()
                .collect(Collectors.groupingBy(User::getGemCount));

        int rank = 1;
        for (int gemCount : usersByGemCount.keySet().stream()
                .sorted((a, b) -> b - a)
                .collect(Collectors.toList())) {
            List<User> usersWithSameGems = usersByGemCount.get(gemCount);
            for (User u : usersWithSameGems) {
                u.setRank(rank);
                userRepository.save(u);
            }
            rank += usersWithSameGems.size();
        }
    }

    public List<User> getTopUsers(int limit) {
        return userRepository.findAll().stream()
                .sorted((a, b) -> b.getGemCount().compareTo(a.getGemCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<User> getLeaderboard() {
        return userRepository.findAll().stream()
                .sorted((a, b) -> b.getGemCount().compareTo(a.getGemCount()))
                .collect(Collectors.toList());
    }
} 