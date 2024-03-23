package com.example.assetproject.service;

import com.example.assetproject.dto.Asset;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final HardwareRepository hardwareRepository;

    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Asset findById(Long id) {
        return assetRepository.findById(id);
    }


}
