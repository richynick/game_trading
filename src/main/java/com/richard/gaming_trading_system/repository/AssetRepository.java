package com.richard.gaming_trading_system.repository;

import com.richard.gaming_trading_system.model.Asset;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AssetRepository {

    private final Map<Long, Asset> assets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Asset save(Asset asset) {
        if (asset.getAssetId() == null) {
            asset.setAssetId(idGenerator.getAndIncrement());
        }
        assets.put(asset.getAssetId(), asset);
        return asset;
    }

    public Optional<Asset> findById(Long id) {
        return Optional.ofNullable(assets.get(id));
    }

    public Optional<Asset> findBySymbol(String symbol) {
        return assets.values().stream()
                .filter(asset -> asset.getSymbol().equals(symbol))
                .findFirst();
    }

    public List<Asset> findAll() {
        return new ArrayList<>(assets.values());
    }
}
