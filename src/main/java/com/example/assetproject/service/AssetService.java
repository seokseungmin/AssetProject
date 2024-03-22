package com.example.assetproject.service;

import com.example.assetproject.dto.Asset;
import com.example.assetproject.dto.Hardware;
import com.example.assetproject.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Asset findOne(Long id) {
        return assetRepository.findOne(id);
    }
}
