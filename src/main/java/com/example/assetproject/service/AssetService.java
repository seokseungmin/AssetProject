package com.example.assetproject.service;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import com.example.assetproject.repository.SoftwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final HardwareRepository hardwareRepository;
    private final SoftwareRepository softwareRepository;

    public List<Asset> findAll() {
        return assetRepository.findAll();
    }

    public Asset findById(Long id) {
        return assetRepository.findById(id);
    }

    public String findAssetCodeById(Long assetIdx) {
        return assetRepository.findAssetCodeById(assetIdx);
    }

    @Transactional
    public void deleteSelectedAsset(List<Long> assetIds) {
        for (Long assetIdx : assetIds) {
            // 각 소프트웨어에 대해 Asset ID(assetIdx)를 찾아낸다.

            String assetCode = assetRepository.findAssetCodeById(assetIdx);

            if(assetIdx != null && assetCode.startsWith("HW")){
                Long hardwareIdx = hardwareRepository.findHardwareIdxByAssetIdx(assetIdx);
                hardwareRepository.deleteById(hardwareIdx);
                assetRepository.deleteById(assetIdx);
            }else if(assetIdx != null && assetCode.startsWith("SW")){
                Long softwareIdx = softwareRepository.findSoftwareIdxByAssetIdx(assetIdx);
                softwareRepository.deleteById(softwareIdx);
                assetRepository.deleteById(assetIdx);
            }
        }
    }

}
