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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssetPriceServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetPriceService assetPriceService;

    private Asset testAsset1;
    private Asset testAsset2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    }

    @Test
    void updateAssetPrices_Success() {
        List<Asset> assets = Arrays.asList(testAsset1, testAsset2);
        when(assetRepository.findAll()).thenReturn(assets);
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        assetPriceService.updateAssetPrices();

        verify(assetRepository, times(2)).save(any(Asset.class));
    }

    @Test
    void updateAssetPrice_Success() {
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        assetPriceService.updateAssetPrice(testAsset1);

        assertNotNull(testAsset1.getCurrentPrice());
        assertTrue(testAsset1.getCurrentPrice().compareTo(BigDecimal.ZERO) > 0);
        verify(assetRepository).save(testAsset1);
    }

    @Test
    void updateAssetPrice_MinimumPrice() {
        testAsset1.setCurrentPrice(new BigDecimal("0.01"));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        assetPriceService.updateAssetPrice(testAsset1);

        assertNotNull(testAsset1.getCurrentPrice());
        assertTrue(testAsset1.getCurrentPrice().compareTo(new BigDecimal("0.01")) >= 0);
        verify(assetRepository).save(testAsset1);
    }

    @Test
    void generateRandomPriceChange_WithinRange() {
        BigDecimal change = assetPriceService.generateRandomPriceChange();

        assertNotNull(change);
        assertTrue(change.compareTo(new BigDecimal("-0.05")) >= 0); // Not less than -5%
        assertTrue(change.compareTo(new BigDecimal("0.05")) <= 0);  // Not greater than +5%
    }

    @Test
    void updateAssetPrice_PriceScale() {
        testAsset1.setCurrentPrice(new BigDecimal("100.123"));
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        assetPriceService.updateAssetPrice(testAsset1);

        assertEquals(2, testAsset1.getCurrentPrice().scale());
        verify(assetRepository).save(testAsset1);
    }

    @Test
    void updateAssetPrice_MultipleUpdates() {
        when(assetRepository.save(any(Asset.class))).thenAnswer(i -> i.getArgument(0));

        // Perform multiple updates and verify price changes
        BigDecimal initialPrice = testAsset1.getCurrentPrice();
        assetPriceService.updateAssetPrice(testAsset1);
        BigDecimal priceAfterFirstUpdate = testAsset1.getCurrentPrice();
        assetPriceService.updateAssetPrice(testAsset1);
        BigDecimal priceAfterSecondUpdate = testAsset1.getCurrentPrice();

        assertNotEquals(initialPrice, priceAfterFirstUpdate);
        assertNotEquals(priceAfterFirstUpdate, priceAfterSecondUpdate);
        verify(assetRepository, times(2)).save(testAsset1);
    }
} 