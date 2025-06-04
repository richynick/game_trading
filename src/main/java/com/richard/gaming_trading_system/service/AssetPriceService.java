package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.Asset;
import com.richard.gaming_trading_system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
public class AssetPriceService {

    @Autowired
    private AssetRepository assetRepository;

    private final Random random = new Random();
    private static final BigDecimal MAX_PRICE_CHANGE_PERCENT = new BigDecimal("0.05"); // 5% max change
    private static final int PRICE_SCALE = 2;

    @Scheduled(fixedRate = 60000) // Update prices every minute
    public void updateAssetPrices() {
        List<Asset> assets = assetRepository.findAll();
        for (Asset asset : assets) {
            updateAssetPrice(asset);
        }
    }

    private void updateAssetPrice(Asset asset) {
        BigDecimal currentPrice = asset.getCurrentPrice();
        BigDecimal changePercent = generateRandomPriceChange();
        BigDecimal priceChange = currentPrice.multiply(changePercent);
        
        // Ensure price doesn't go below 0.01
        BigDecimal newPrice = currentPrice.add(priceChange).max(new BigDecimal("0.01"));
        newPrice = newPrice.setScale(PRICE_SCALE, RoundingMode.HALF_UP);
        
        asset.setCurrentPrice(newPrice);
        assetRepository.save(asset);
    }

    private BigDecimal generateRandomPriceChange() {
        // Generate a random percentage between -5% and +5%
        double randomValue = random.nextDouble() * 2 - 1; // Value between -1 and 1
        return MAX_PRICE_CHANGE_PERCENT.multiply(BigDecimal.valueOf(randomValue));
    }
} 