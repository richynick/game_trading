package com.richard.gaming_trading_system.service;

import com.richard.gaming_trading_system.dto.CreateAssetRequest;
import com.richard.gaming_trading_system.exception.AssetNotFoundException;
import com.richard.gaming_trading_system.model.Asset;
import com.richard.gaming_trading_system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public Asset createAsset(CreateAssetRequest request) {
        Asset asset = new Asset();
        asset.setSymbol(request.getSymbol());
        asset.setName(request.getName());
        asset.setCurrentPrice(request.getInitialPrice());
        return assetRepository.save(asset);
    }

    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found: " + assetId));
    }

    public Asset getAssetBySymbol(String symbol) {
        return assetRepository.findBySymbol(symbol)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with symbol: " + symbol));
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
} 