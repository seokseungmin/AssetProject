package com.example.assetproject.service;

import com.example.assetproject.dto.Asset;
import com.example.assetproject.dto.Hardware;
import com.example.assetproject.dto.Software;
import com.example.assetproject.dto.SoftwareAssetDTO;
import com.example.assetproject.form.AssetHardwareUpdateForm;
import com.example.assetproject.form.AssetSoftwareAddForm;
import com.example.assetproject.form.AssetSoftwareUpdateForm;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.SoftwareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoftwareService {

    private final SoftwareRepository softwareRepository;
    private final AssetRepository assetRepository;

    public List<Software> findAll() {
        return softwareRepository.findAll();
    }

    @Transactional
    public Long save(AssetSoftwareAddForm form) {

        // Asset 데이터를 먼저 저장
        Asset asset = new Asset();
        asset.setAssetCode(form.getAssetCode());
        asset.setAssetName(form.getAssetName());
        asset.setAssetStatus(form.getAssetStatus());
        asset.setAssignedDate(form.getAssignedDate());
        asset.setCurrentUser(form.getCurrentUser());
        asset.setDept(form.getDept());
        asset.setLocation(form.getLocation());
        asset.setManufacturer(form.getManufacturer());
        asset.setPreviousUser(form.getPreviousUser());
        asset.setPurchaseDate(form.getPurchaseDate());
        asset.setReturnDate(form.getReturnDate());
        asset.setSn(form.getSn());

        // form에서 받은 나머지 asset 관련 데이터 설정
        assetRepository.save(asset); // MyBatis 사용 시, 적절한 매퍼 메소드 호출

        Long assetIdx = asset.getAssetIdx();

        // Hardware 데이터 저장, Asset 저장으로부터 생성된 ID(또는 assetCode)를 사용
        Software software = new Software();

        software.setAssetIdx(assetIdx); // 여기에서 assetIdx 설정
        software.setExpiryDate(form.getExpiryDate());
        software.setNote(form.getNote());

        softwareRepository.save(software); // MyBatis 사용 시, 적절한 매퍼 메소드 호출
        return assetIdx;
    }

    @Transactional
    public void update(Long softwareIdx, AssetSoftwareUpdateForm form) {

        Software software = softwareRepository.findById(softwareIdx);
        if (software == null) {
            throw new IllegalArgumentException("Hardware not found with id: " + softwareIdx);
        }

        // Asset 엔티티 업데이트 로직 추가
        Asset asset = assetRepository.findById(software.getAssetIdx());
        log.debug("asset ={}", asset);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found with id: " + software.getAssetIdx());
        }

        // form으로부터 받은 값으로 Asset 엔티티 업데이트
        asset.setAssetCode(form.getAssetCode());
        asset.setAssetName(form.getAssetName());
        asset.setAssetStatus(form.getAssetStatus());
        asset.setAssetType(form.getAssetType());
        asset.setAssignedDate(form.getAssignedDate());
        asset.setCurrentUser(form.getCurrentUser());
        asset.setDept(form.getDept());
        asset.setLocation(form.getLocation());
        asset.setManufacturer(form.getManufacturer());
        asset.setPreviousUser(form.getPreviousUser());
        asset.setPurchaseDate(form.getPurchaseDate());
        asset.setReturnDate(form.getReturnDate());
        asset.setSn(form.getSn());

        assetRepository.update(asset);

        software.update(form.getExpiryDate(), form.getNote());
        softwareRepository.update(software);
    }

    public SoftwareAssetDTO findSoftwareById(Long softwareIdx) {
        return softwareRepository.findSoftwareById(softwareIdx);
    }


}
