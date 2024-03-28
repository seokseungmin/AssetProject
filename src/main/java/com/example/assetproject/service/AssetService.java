package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.dto.HardwareAssetDTO;
import com.example.assetproject.dto.SoftwareAssetDTO;
import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.History;
import com.example.assetproject.entity.Software;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import com.example.assetproject.repository.HistoryRepository;
import com.example.assetproject.repository.SoftwareRepository;
import com.example.assetproject.types.Action;
import com.example.assetproject.types.Type;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final HardwareRepository hardwareRepository;
    private final SoftwareRepository softwareRepository;
    private final HistoryRepository historyRepository;

//    public List<Asset> findAll() {
//        return assetRepository.findAll();
//    }

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
            String assetType = assetRepository.findAssetTypeById(assetIdx);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String username = ((AdminUserDetails) principal).getUsername();

            LocalDateTime now = LocalDateTime.now();

            History history = new History();

            if(assetIdx != null && assetCode.startsWith("HW")){
                Long hardwareIdx = hardwareRepository.findHardwareIdxByAssetIdx(assetIdx);
                Asset asset = assetRepository.findById(assetIdx);
                Hardware hardware = hardwareRepository.findById(hardwareIdx);
                hardwareRepository.deleteById(hardwareIdx);
                assetRepository.deleteById(assetIdx);

                HardwareAssetDTO deleteHardware = new HardwareAssetDTO();
                deleteHardware.setAssetCode(asset.getAssetCode());
                deleteHardware.setAssetName(asset.getAssetName());
                deleteHardware.setAssetType(asset.getAssetType());
                deleteHardware.setAssetStatus(asset.getAssetStatus());
                deleteHardware.setSn(asset.getSn());
                deleteHardware.setLocation(asset.getLocation());
                deleteHardware.setDept(asset.getDept());
                deleteHardware.setPurchaseDate(asset.getPurchaseDate());
                deleteHardware.setAssignedDate(asset.getAssignedDate());
                deleteHardware.setReturnDate(asset.getReturnDate());
                deleteHardware.setCurrentUser(asset.getCurrentUser());
                deleteHardware.setPreviousUser(asset.getPreviousUser());
                deleteHardware.setManufacturer(asset.getManufacturer());

                deleteHardware.setCpu(hardware.getCpu());
                deleteHardware.setSsd(hardware.getSsd());
                deleteHardware.setHdd(hardware.getHdd());
                deleteHardware.setMemory(hardware.getMemory());
                deleteHardware.setNote(hardware.getNote());

                Map<String, Object> HardwareInfo = captureHardwareInfo(deleteHardware);
                String historyJson = HistoryJson(HardwareInfo);
                saveHardwareHistory(asset.getAssetCode(), String.valueOf(asset.getAssetType()), String.valueOf(Action.DELETE), username, LocalDateTime.now(), historyJson);
            }else if(assetIdx != null && assetCode.startsWith("SW")){
                Long softwareIdx = softwareRepository.findSoftwareIdxByAssetIdx(assetIdx);
                Asset asset = assetRepository.findById(assetIdx);
                Software software = softwareRepository.findById(softwareIdx);
                softwareRepository.deleteById(softwareIdx);
                assetRepository.deleteById(assetIdx);

                SoftwareAssetDTO deleteSoftware = new SoftwareAssetDTO();
                deleteSoftware.setAssetCode(asset.getAssetCode());
                deleteSoftware.setAssetName(asset.getAssetName());
                deleteSoftware.setAssetType(asset.getAssetType());
                deleteSoftware.setAssetStatus(asset.getAssetStatus());
                deleteSoftware.setSn(asset.getSn());
                deleteSoftware.setLocation(asset.getLocation());
                deleteSoftware.setDept(asset.getDept());
                deleteSoftware.setPurchaseDate(asset.getPurchaseDate());
                deleteSoftware.setAssignedDate(asset.getAssignedDate());
                deleteSoftware.setReturnDate(asset.getReturnDate());
                deleteSoftware.setCurrentUser(asset.getCurrentUser());
                deleteSoftware.setPreviousUser(asset.getPreviousUser());
                deleteSoftware.setManufacturer(asset.getManufacturer());

                deleteSoftware.setExpiryDate(software.getExpiryDate());
                deleteSoftware.setNote(software.getNote());

                Map<String, Object> createSoftwareInfo = captureSoftwareInfo(deleteSoftware);

                String historyJson = HistoryJson(createSoftwareInfo);
                saveSoftwareHistory(asset.getAssetCode(), String.valueOf(Type.SOFTWARE), String.valueOf(Action.DELETE), username, LocalDateTime.now(), historyJson);
            }
        }
    }

    private void saveSoftwareHistory(String assetCode, String assetType, String action, String changedBy, LocalDateTime changedDate, String historyJson) {
        History history = new History();
        history.setAssetCode(assetCode);
        history.setAssetType(Type.valueOf(assetType));
        history.setAction(Action.valueOf(action));
        history.setChangedBy(changedBy);
        history.setChangedDate(changedDate);
        history.setAssetJSON(historyJson);
        historyRepository.save(history);
    }
    private Map<String, Object> captureSoftwareInfo(SoftwareAssetDTO software) {
        Map<String, Object> softwareInfo = new HashMap<>();
        softwareInfo.put("assetCode", software.getAssetCode());
        softwareInfo.put("assetName", software.getAssetName());
        softwareInfo.put("assetType", software.getAssetType());
        softwareInfo.put("assetStatus", software.getAssetStatus());
        softwareInfo.put("sn", software.getSn());
        softwareInfo.put("location", software.getLocation());
        softwareInfo.put("dept", software.getDept());
        softwareInfo.put("purchaseDate", software.getPurchaseDate());
        softwareInfo.put("assignedDate", software.getAssignedDate());
        softwareInfo.put("returnDate", software.getReturnDate());
        softwareInfo.put("currentUser", software.getCurrentUser());
        softwareInfo.put("previousUser", software.getPreviousUser());
        softwareInfo.put("manufacturer", software.getManufacturer());

        softwareInfo.put("expiryDate", software.getExpiryDate());
        softwareInfo.put("note", software.getNote());
        return softwareInfo;
    }

    private void saveHardwareHistory(String assetCode, String assetType, String action, String changedBy, LocalDateTime changedDate, String historyJson) {
        History history = new History();
        history.setAssetCode(assetCode);
        history.setAssetType(Type.valueOf(assetType));
        history.setAction(Action.valueOf(action));
        history.setChangedBy(changedBy);
        history.setChangedDate(changedDate);
        history.setAssetJSON(historyJson);
        historyRepository.save(history);
    }

    private String HistoryJson(Map<String, Object> Info) {
        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("AssetInfo", Info);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        try {
            return objectMapper.writeValueAsString(historyMap);
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON string for history", e);
            return null;
        }
    }

    private Map<String, Object> captureHardwareInfo(HardwareAssetDTO hardware) {
        Map<String, Object> hardwareInfo = new HashMap<>();
        hardwareInfo.put("assetCode", hardware.getAssetCode());
        hardwareInfo.put("assetName", hardware.getAssetName());
        hardwareInfo.put("assetType", hardware.getAssetType());
        hardwareInfo.put("assetStatus", hardware.getAssetStatus());
        hardwareInfo.put("sn", hardware.getSn());
        hardwareInfo.put("location", hardware.getLocation());
        hardwareInfo.put("dept", hardware.getDept());
        hardwareInfo.put("purchaseDate", hardware.getPurchaseDate());
        hardwareInfo.put("assignedDate", hardware.getAssignedDate());
        hardwareInfo.put("returnDate", hardware.getReturnDate());
        hardwareInfo.put("currentUser", hardware.getCurrentUser());
        hardwareInfo.put("previousUser", hardware.getPreviousUser());
        hardwareInfo.put("manufacturer", hardware.getManufacturer());

        hardwareInfo.put("cpu", hardware.getCpu());
        hardwareInfo.put("ssd", hardware.getSsd());
        hardwareInfo.put("hdd", hardware.getHdd());
        hardwareInfo.put("memory", hardware.getMemory());
        hardwareInfo.put("note", hardware.getNote());
        return hardwareInfo;
    }
//    public List<Asset> findAllWithPaging(int pageNum, int pageSize) {
//
//        int offset = (pageNum - 1) * pageSize;
//        Map<String, Object> params = new HashMap<>();
//        params.put("offset", offset);
//        params.put("pageSize", pageSize);
//        return assetRepository.findAllWithPaging(params);
//    }
//
//    public int countAll() {
//        return assetRepository.countAll();
//    }

    public List<Asset> findAllWithPagingAndFilter(int pageNum, int pageSize, String category, String keyword, String sort, String order) {
        int offset = (pageNum - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("category", category);
        params.put("sort", sort);
        params.put("order", order);
        params.put("keyword", "%" + (keyword == null ? "" : keyword) + "%"); // 와일드카드 검색을 위해 키워드 양쪽에 % 추가
        return assetRepository.findAllWithPagingAndFilter(params);
    }

    public int countAllFiltered(String category, String keyword) {
        return assetRepository.countAllFiltered(category, "%" + (keyword == null ? "" : keyword) + "%");
    }
}
