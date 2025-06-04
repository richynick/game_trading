package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.model.Asset;
import com.richard.gaming_trading_system.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetPriceServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetPriceService assetPriceService;

    private Asset asset1;
    private Asset asset2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test assets
        asset1 = new Asset();
        asset1.setAssetId(1L);
        asset1.setName("Asset 1");
        asset1.setCurrentPrice(new BigDecimal("100.00"));

        asset2 = new Asset();
        asset2.setAssetId(2L);
        asset2.setName("Asset 2");
        asset2.setCurrentPrice(new BigDecimal("50.00"));

        when(assetRepository.findAll()).thenReturn(Arrays.asList(asset1, asset2));
    }

    @Test
    void updateAssetPrices() {
        // Execute the scheduled method
        assetPriceService.updateAssetPrices();

        // Verify that save was called for each asset
        verify(assetRepository, times(2)).save(any(Asset.class));

        // Verify that the prices were updated and are within the expected range
        verify(assetRepository).save(argThat(asset -> {
            BigDecimal price = asset.getCurrentPrice();
            return price.compareTo(new BigDecimal("0.01")) >= 0 &&
                   price.compareTo(new BigDecimal("105.00")) <= 0;
        }));
    }

    @Test
    void updateAssetPrice() {
        // Test price increase
        Asset asset = new Asset();
        asset.setCurrentPrice(new BigDecimal("100.00"));
        assetPriceService.updateAssetPrice(asset);
        assertTrue(asset.getCurrentPrice().compareTo(new BigDecimal("0.01")) >= 0);
        assertTrue(asset.getCurrentPrice().compareTo(new BigDecimal("105.00")) <= 0);

        // Test price decrease
        asset = new Asset();
        asset.setCurrentPrice(new BigDecimal("100.00"));
        assetPriceService.updateAssetPrice(asset);
        assertTrue(asset.getCurrentPrice().compareTo(new BigDecimal("0.01")) >= 0);
        assertTrue(asset.getCurrentPrice().compareTo(new BigDecimal("105.00")) <= 0);

        // Test minimum price
        asset = new Asset();
        asset.setCurrentPrice(new BigDecimal("0.02"));
        assetPriceService.updateAssetPrice(asset);
        assertTrue(asset.getCurrentPrice().compareTo(new BigDecimal("0.01")) >= 0);
    }

    @Test
    void generateRandomPriceChange() {
        // Test multiple price changes to ensure they're within bounds
        for (int i = 0; i < 100; i++) {
            BigDecimal change = assetPriceService.generateRandomPriceChange();
            assertTrue(change.compareTo(new BigDecimal("-0.05")) >= 0);
            assertTrue(change.compareTo(new BigDecimal("0.05")) <= 0);
        }
    }
} 