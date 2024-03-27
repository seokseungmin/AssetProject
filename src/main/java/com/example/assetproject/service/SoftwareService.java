package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.History;
import com.example.assetproject.entity.Software;
import com.example.assetproject.dto.SoftwareAssetDTO;
import com.example.assetproject.form.AssetSoftwareAddForm;
import com.example.assetproject.form.AssetSoftwareUpdateForm;
import com.example.assetproject.repository.AssetRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class SoftwareService {

    private final SoftwareRepository softwareRepository;
    private final AssetRepository assetRepository;
    private final HistoryRepository historyRepository;

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
        Software software = new Software();

        software.setAssetIdx(assetIdx); // 여기에서 assetIdx 설정
        software.setExpiryDate(form.getExpiryDate());
        software.setNote(form.getNote());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((AdminUserDetails) principal).getUsername();

        LocalDateTime now = LocalDateTime.now();

        softwareRepository.save(software); // MyBatis 사용 시, 적절한 매퍼 메소드 호출

        History history = new History();
        history.setAssetCode(form.getAssetCode());
        history.setAssetType(form.getAssetType());
        history.setAction(Action.CREATE);
        history.setChangedBy(username);
        history.setChangedDate(now);
        history.setAssetJSON(null);

        historyRepository.save(history);
        return assetIdx;
    }

    @Transactional
    public void update(Long softwareIdx, AssetSoftwareUpdateForm form) {
        // 업데이트 전 HardwareAssetDTO 정보 불러오기
        SoftwareAssetDTO originalSoftware = softwareRepository.findSoftwareById(softwareIdx);
        if (originalSoftware == null) {
            throw new IllegalArgumentException("Software not found with id: " + softwareIdx);
        }

        // 업데이트 전 상태 캡처
        Map<String, Object> beforeSoftwareInfo = captureSoftwareInfo(originalSoftware);

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

        SoftwareAssetDTO changedSoftware = new SoftwareAssetDTO();
        changedSoftware.setAssetCode(form.getAssetCode());
        changedSoftware.setAssetName(form.getAssetName());
        changedSoftware.setAssetType(form.getAssetType());
        changedSoftware.setAssetStatus(form.getAssetStatus());
        changedSoftware.setSn(form.getSn());
        changedSoftware.setLocation(form.getLocation());
        changedSoftware.setDept(form.getDept());
        changedSoftware.setPurchaseDate(form.getPurchaseDate());
        changedSoftware.setAssignedDate(form.getAssignedDate());
        changedSoftware.setReturnDate(form.getReturnDate());
        changedSoftware.setCurrentUser(form.getCurrentUser());
        changedSoftware.setPreviousUser(form.getPreviousUser());
        changedSoftware.setManufacturer(form.getManufacturer());

        changedSoftware.setExpiryDate(form.getExpiryDate());
        changedSoftware.setNote(form.getNote());

        softwareRepository.update(software);

        // 업데이트 후 상태 캡처
        Map<String, Object> afterSoftwareInfo = captureSoftwareInfo(changedSoftware);

        // 사용자 정보 캡처
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((AdminUserDetails) authentication.getPrincipal()).getUsername();

        // JSON 문자열 생성 및 History 객체 생성 및 저장
        String historyJson = createHistoryJson(beforeSoftwareInfo, afterSoftwareInfo);
        saveSoftwareHistory(form.getAssetCode(), String.valueOf(form.getAssetType()), username, LocalDateTime.now(), historyJson);

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

    private void saveSoftwareHistory(String assetCode, String assetType, String changedBy, LocalDateTime changedDate, String historyJson) {
        History history = new History();
        history.setAssetCode(assetCode);
        history.setAssetType(Type.valueOf(assetType));
        history.setAction(Action.UPDATE);
        history.setChangedBy(changedBy);
        history.setChangedDate(changedDate);
        history.setAssetJSON(historyJson);
        historyRepository.save(history);
    }

    public SoftwareAssetDTO findSoftwareById(Long softwareIdx) {
        return softwareRepository.findSoftwareById(softwareIdx);
    }

    @Transactional
    public void deleteSelectedSoftware(List<Long> softwareIds) {
        for (Long softwareIdx : softwareIds) {
            // 각 소프트웨어에 대해 Asset ID(assetIdx)를 찾아낸다.
            Long assetIdx = softwareRepository.findAssetIdxBySoftwareIdx(softwareIdx);
            String assetCode = softwareRepository.findAssetCodeBySoftwareIdx(softwareIdx);
            if (assetIdx != null) {
                // 먼저 Software를 삭제한다.
                softwareRepository.deleteById(softwareIdx);
                // 연결된 Asset도 삭제한다.
                assetRepository.deleteById(assetIdx);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Object principal = authentication.getPrincipal();
                String username = ((AdminUserDetails) principal).getUsername();

                LocalDateTime now = LocalDateTime.now();

                History history = new History();
                history.setAssetCode(assetCode);
                history.setAssetType(Type.SOFTWARE);
                history.setAction(Action.DELETE);
                history.setChangedBy(username);
                history.setChangedDate(now);
                history.setAssetJSON(null);
                historyRepository.save(history);
            }
        }
    }

    public Long findSoftwareIdxByAssetIdx(Long assetIdx) {
        return softwareRepository.findSoftwareIdxByAssetIdx(assetIdx);
    }

//    public List<Software> findAllWithPaging(int pageNum, int pageSize) {
//        int offset = (pageNum - 1) * pageSize;
//        Map<String, Object> params = new HashMap<>();
//        params.put("offset", offset);
//        params.put("pageSize", pageSize);
//        return softwareRepository.findAllWithPaging(params);
//    }
//
//    public int countAll() {
//        return softwareRepository.countAll();
//    }

    public List<Software> findAllWithPagingAndFilter(int pageNum, int pageSize, String category, String keyword, String sort, String order) {
        int offset = (pageNum - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("category", category);
        params.put("sort", sort);
        params.put("order", order);
        params.put("keyword", "%" + (keyword == null ? "" : keyword) + "%"); // 와일드카드 검색을 위해 키워드 양쪽에 % 추가
        return softwareRepository.findAllWithPagingAndFilter(params);
    }

    public int countAllFiltered(String category, String keyword) {
        return softwareRepository.countAllFiltered(category, "%" + (keyword == null ? "" : keyword) + "%");
    }
}
