package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreateAssetRequest;
import com.richard.gaming_trading_system.exception.AssetNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;
    private CreateAssetRequest createAssetRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testAsset = new Asset();
        testAsset.setAssetId(1L);
        testAsset.setSymbol("TEST");
        testAsset.setName("Test Asset");
        testAsset.setCurrentPrice(new BigDecimal("100.00"));

        createAssetRequest = new CreateAssetRequest();
        createAssetRequest.setSymbol("TEST");
        createAssetRequest.setName("Test Asset");
        createAssetRequest.setInitialPrice(new BigDecimal("100.00"));
    }

    @Test
    void createAsset_Success() {
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        Asset result = assetService.createAsset(createAssetRequest);

        assertNotNull(result);
        assertEquals(testAsset.getSymbol(), result.getSymbol());
        assertEquals(testAsset.getName(), result.getName());
        assertEquals(testAsset.getCurrentPrice(), result.getCurrentPrice());
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void getAssetById_Success() {
        when(assetRepository.findById(testAsset.getAssetId())).thenReturn(Optional.of(testAsset));

        Asset result = assetService.getAssetById(testAsset.getAssetId());

        assertNotNull(result);
        assertEquals(testAsset.getAssetId(), result.getAssetId());
        assertEquals(testAsset.getSymbol(), result.getSymbol());
        assertEquals(testAsset.getName(), result.getName());
    }

    @Test
    void getAssetById_NotFound() {
        when(assetRepository.findById(testAsset.getAssetId())).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> assetService.getAssetById(testAsset.getAssetId()));
    }

    @Test
    void getAssetBySymbol_Success() {
        when(assetRepository.findBySymbol(testAsset.getSymbol())).thenReturn(Optional.of(testAsset));

        Asset result = assetService.getAssetBySymbol(testAsset.getSymbol());

        assertNotNull(result);
        assertEquals(testAsset.getAssetId(), result.getAssetId());
        assertEquals(testAsset.getSymbol(), result.getSymbol());
        assertEquals(testAsset.getName(), result.getName());
    }

    @Test
    void getAssetBySymbol_NotFound() {
        when(assetRepository.findBySymbol(testAsset.getSymbol())).thenReturn(Optional.empty());

        assertThrows(AssetNotFoundException.class, () -> assetService.getAssetBySymbol(testAsset.getSymbol()));
    }

    @Test
    void getAllAssets_Success() {
        List<Asset> expectedAssets = Arrays.asList(testAsset);
        when(assetRepository.findAll()).thenReturn(expectedAssets);

        List<Asset> result = assetService.getAllAssets();

        assertNotNull(result);
        assertEquals(expectedAssets.size(), result.size());
        assertEquals(expectedAssets.get(0).getAssetId(), result.get(0).getAssetId());
        assertEquals(expectedAssets.get(0).getSymbol(), result.get(0).getSymbol());
    }
} 