package com.example.assetproject.service;

import com.example.assetproject.dto.AdminUserDetails;
import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.History;
import com.example.assetproject.repository.AssetRepository;
import com.example.assetproject.repository.HardwareRepository;
import com.example.assetproject.repository.HistoryRepository;
import com.example.assetproject.repository.SoftwareRepository;
import com.example.assetproject.types.Action;
import com.example.assetproject.types.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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
                hardwareRepository.deleteById(hardwareIdx);
                assetRepository.deleteById(assetIdx);

                history.setAssetCode(assetCode);
                history.setAssetType(Type.valueOf(assetType));
                history.setAction(Action.DELETE);
                history.setChangedBy(username);
                history.setChangedDate(now);
                history.setAssetJSON(null);
                historyRepository.save(history);
            }else if(assetIdx != null && assetCode.startsWith("SW")){
                Long softwareIdx = softwareRepository.findSoftwareIdxByAssetIdx(assetIdx);
                softwareRepository.deleteById(softwareIdx);
                assetRepository.deleteById(assetIdx);

                history.setAssetCode(assetCode);
                history.setAssetType(Type.valueOf(assetType));
                history.setAction(Action.DELETE);
                history.setChangedBy(username);
                history.setChangedDate(now);
                history.setAssetJSON(null);
                historyRepository.save(history);
            }
        }
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
