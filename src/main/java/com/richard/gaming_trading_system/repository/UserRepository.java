package com.richard.gaming_trading_system.repository;

import com.richard.gaming_trading_system.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(idGenerator.getAndIncrement());
        }
        users.put(user.getUserId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findAllOrderByGemCountDesc() {
        return users.values().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getGemCount(), u1.getGemCount()))
                .toList();
    }

    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}
