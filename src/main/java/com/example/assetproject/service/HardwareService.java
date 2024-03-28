package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.dto.HardwareAssetDTO;
import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.History;
import com.example.assetproject.form.AssetHardwareAddForm;
import com.example.assetproject.form.AssetHardwareUpdateForm;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import com.example.assetproject.repository.HistoryRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class HardwareService {

    private final HardwareRepository hardwareRepository;
    private final AssetRepository assetRepository;
    private final HistoryRepository historyRepository;

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
        // form에서 받은 hardware 관련 데이터 설정

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((AdminUserDetails) principal).getUsername();

        LocalDateTime now = LocalDateTime.now();

        Hardware hardware = new Hardware();
        hardware.setAssetIdx(assetIdx); // 여기에서 assetIdx 설정
        hardware.setCpu(form.getCpu());
        hardware.setSsd(form.getSsd());
        hardware.setHdd(form.getHdd());
        hardware.setMemory(form.getMemory());
        hardware.setNote(form.getNote());

        HardwareAssetDTO createHardware = new HardwareAssetDTO();
        createHardware.setAssetCode(form.getAssetCode());
        createHardware.setAssetName(form.getAssetName());
        createHardware.setAssetType(form.getAssetType());
        createHardware.setAssetStatus(form.getAssetStatus());
        createHardware.setSn(form.getSn());
        createHardware.setLocation(form.getLocation());
        createHardware.setDept(form.getDept());
        createHardware.setPurchaseDate(form.getPurchaseDate());
        createHardware.setAssignedDate(form.getAssignedDate());
        createHardware.setReturnDate(form.getReturnDate());
        createHardware.setCurrentUser(form.getCurrentUser());
        createHardware.setPreviousUser(form.getPreviousUser());
        createHardware.setManufacturer(form.getManufacturer());

        createHardware.setCpu(form.getCpu());
        createHardware.setSsd(form.getSsd());
        createHardware.setHdd(form.getHdd());
        createHardware.setMemory(form.getMemory());
        createHardware.setNote(form.getNote());

        Map<String, Object> HardwareInfo = captureHardwareInfo(createHardware);
        String historyJson = HistoryJson(HardwareInfo);
        saveHardwareHistory(form.getAssetCode(), String.valueOf(form.getAssetType()), String.valueOf(Action.CREATE), username, LocalDateTime.now(), historyJson);

        hardwareRepository.save(hardware); // MyBatis 사용 시, 적절한 매퍼 메소드 호출
        return assetIdx;
    }

    public Hardware findById(Long hardwareIdx) {
        return hardwareRepository.findById(hardwareIdx);
    }

    @Transactional
    public void update(Long hardwareIdx, AssetHardwareUpdateForm form) {
        // 업데이트 전 HardwareAssetDTO 정보 불러오기
        HardwareAssetDTO originalHardware = hardwareRepository.findHardwareById(hardwareIdx);
        if (originalHardware == null) {
            throw new IllegalArgumentException("Hardware not found with id: " + hardwareIdx);
        }

        // 업데이트 전 상태 캡처
        Map<String, Object> beforeHardwareInfo = captureHardwareInfo(originalHardware);

        // 실제 Hardware 엔티티 업데이트 로직
        Hardware hardware = hardwareRepository.findById(hardwareIdx);
        if (hardware == null) {
            throw new IllegalArgumentException("Hardware entity not found with id: " + hardwareIdx);
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


        // Hardware 엔티티 업데이트
        hardware.update(form.getCpu(), form.getSsd(), form.getHdd(), form.getMemory(), form.getNote());

        HardwareAssetDTO changedHardware = new HardwareAssetDTO();
        changedHardware.setAssetCode(form.getAssetCode());
        changedHardware.setAssetName(form.getAssetName());
        changedHardware.setAssetType(form.getAssetType());
        changedHardware.setAssetStatus(form.getAssetStatus());
        changedHardware.setSn(form.getSn());
        changedHardware.setLocation(form.getLocation());
        changedHardware.setDept(form.getDept());
        changedHardware.setPurchaseDate(form.getPurchaseDate());
        changedHardware.setAssignedDate(form.getAssignedDate());
        changedHardware.setReturnDate(form.getReturnDate());
        changedHardware.setCurrentUser(form.getCurrentUser());
        changedHardware.setPreviousUser(form.getPreviousUser());
        changedHardware.setManufacturer(form.getManufacturer());

        changedHardware.setCpu(form.getCpu());
        changedHardware.setSsd(form.getSsd());
        changedHardware.setHdd(form.getHdd());
        changedHardware.setMemory(form.getMemory());
        changedHardware.setNote(form.getNote());
        hardwareRepository.update(hardware);

        // 업데이트 후 상태 캡처
        Map<String, Object> afterHardwareInfo = captureHardwareInfo(changedHardware);

        // 사용자 정보 캡처
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((AdminUserDetails) authentication.getPrincipal()).getUsername();

        // JSON 문자열 생성 및 History 객체 생성 및 저장
        String historyJson = createHistoryJson(beforeHardwareInfo, afterHardwareInfo);
        saveHardwareHistory(form.getAssetCode(), String.valueOf(form.getAssetType()), String.valueOf(Action.UPDATE), username, LocalDateTime.now(), historyJson);
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

    private String createHistoryJson(Map<String, Object> beforeInfo, Map<String, Object> afterInfo) {
        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("before", beforeInfo);
        historyMap.put("after", afterInfo);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        try {
            return objectMapper.writeValueAsString(historyMap);
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON string for history", e);
            return null;
        }
    }

    private String HistoryJson(Map<String, Object> HardwareInfo) {
        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("HardwareInfo", HardwareInfo);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        try {
            return objectMapper.writeValueAsString(historyMap);
        } catch (JsonProcessingException e) {
            log.error("Error creating JSON string for history", e);
            return null;
        }
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

    public HardwareAssetDTO findHardwareById(Long hardwareIdx) {
        return hardwareRepository.findHardwareById(hardwareIdx);
    }

    @Transactional
    public void deleteSelectedHardware(List<Long> hardwareIds) {
        for (Long hardwareIdx : hardwareIds) {
            // 각 소프트웨어에 대해 Asset ID(assetIdx)를 찾아낸다.
            Long assetIdx = hardwareRepository.findAssetIdxByHardwareIdx(hardwareIdx);
            String assetCode = hardwareRepository.findAssetCodeByHardwareIdx(hardwareIdx);
            String assetType = hardwareRepository.findAssetTypeByHardwareIdx(hardwareIdx);
            Asset asset = assetRepository.findById(assetIdx);
            Hardware hardware = hardwareRepository.findById(hardwareIdx);

            if (assetIdx != null) {
                // 먼저 Software를 삭제한다.
                hardwareRepository.deleteById(hardwareIdx);
                // 연결된 Asset도 삭제한다.
                assetRepository.deleteById(assetIdx);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();
                String username = ((AdminUserDetails) principal).getUsername();

                LocalDateTime now = LocalDateTime.now();

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
            }
        }
    }

    public Long findHardwareIdxByAssetIdx(Long assetIdx) {
        return hardwareRepository.findHardwareIdxByAssetIdx(assetIdx);
    }

//    public List<Hardware> findAllWithPaging(int pageNum, int pageSize) {
//
//        int offset = (pageNum - 1) * pageSize;
//        Map<String, Object> params = new HashMap<>();
//        params.put("offset", offset);
//        params.put("pageSize", pageSize);
//        return hardwareRepository.findAllWithPaging(params);
//    }
//
//    public int countAll() {
//        return hardwareRepository.countAll();
//    }
//}

    public List<Hardware> findAllWithPagingAndFilter(int pageNum, int pageSize, String category, String keyword, String sort, String order) {
        int offset = (pageNum - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("category", category);
        params.put("sort", sort);
        params.put("order", order);
        params.put("keyword", "%" + (keyword == null ? "" : keyword) + "%"); // 와일드카드 검색을 위해 키워드 양쪽에 % 추가
        return hardwareRepository.findAllWithPagingAndFilter(params);
    }

    public int countAllFiltered(String category, String keyword) {
        return hardwareRepository.countAllFiltered(category, "%" + (keyword == null ? "" : keyword) + "%");
    }
}

