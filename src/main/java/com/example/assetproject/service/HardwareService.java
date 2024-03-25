package com.example.assetproject.service;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.dto.HardwareAssetDTO;
import com.example.assetproject.form.AssetHardwareAddForm;
import com.example.assetproject.form.AssetHardwareUpdateForm;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HardwareService {

    private final HardwareRepository hardwareRepository;
    private final AssetRepository assetRepository;

//    public List<Hardware> findAll() {
//        return hardwareRepository.findAll();
//    }

    // Hardware 객체를 받아서 저장하는 메서드
    @Transactional
    public Long save(AssetHardwareAddForm form) {
        // Asset 데이터를 먼저 저장
        Asset asset = new Asset();
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
        // form에서 받은 나머지 asset 관련 데이터 설정
        assetRepository.save(asset); // MyBatis 사용 시, 적절한 매퍼 메소드 호출

        Long assetIdx = asset.getAssetIdx();

        // Hardware 데이터 저장, Asset 저장으로부터 생성된 ID(또는 assetCode)를 사용
        Hardware hardware = new Hardware();
        hardware.setAssetIdx(assetIdx); // 여기에서 assetIdx 설정
        hardware.setCpu(form.getCpu());
        hardware.setSsd(form.getSsd());
        hardware.setHdd(form.getHdd());
        hardware.setMemory(form.getMemory());
        hardware.setNote(form.getNote());
        // form에서 받은 hardware 관련 데이터 설정
        // ...
        hardwareRepository.save(hardware); // MyBatis 사용 시, 적절한 매퍼 메소드 호출
        return assetIdx;
    }

    public Hardware findById(Long hardwareIdx) {
        return hardwareRepository.findById(hardwareIdx);
    }

    @Transactional
    public void update(Long hardwareIdx, AssetHardwareUpdateForm form) {

        Hardware hardware = hardwareRepository.findById(hardwareIdx);
        if (hardware == null) {
            throw new IllegalArgumentException("Hardware not found with id: " + hardwareIdx);
        }

        // Asset 엔티티 업데이트 로직 추가
        Asset asset = assetRepository.findById(hardware.getAssetIdx());
        log.debug("asset ={}", asset);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found with id: " + hardware.getAssetIdx());
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

        hardware.update(form.getCpu(), form.getSsd(), form.getHdd(), form.getMemory(), form.getNote());
        hardwareRepository.update(hardware);
    }


    public HardwareAssetDTO findHardwareById(Long hardwareIdx) {
        return hardwareRepository.findHardwareById(hardwareIdx);
    }

    @Transactional
    public void deleteSelectedHardware(List<Long> hardwareIds) {
        for (Long hardwareIdx : hardwareIds) {
            // 각 소프트웨어에 대해 Asset ID(assetIdx)를 찾아낸다.
            Long assetIdx = hardwareRepository.findAssetIdxByHardwareIdx(hardwareIdx);
            if (assetIdx != null) {
                // 먼저 Software를 삭제한다.
                hardwareRepository.deleteById(hardwareIdx);
                // 연결된 Asset도 삭제한다.
                assetRepository.deleteById(assetIdx);
            }
        }
    }

    public Long findHardwareIdxByAssetIdx(Long assetIdx) {
        return hardwareRepository.findHardwareIdxByAssetIdx(assetIdx);
    }

    public List<Hardware> findAllWithPaging(int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        return hardwareRepository.findAllWithPaging(params);
    }

    public int countAll() {
        return hardwareRepository.countAll();
    }
}
