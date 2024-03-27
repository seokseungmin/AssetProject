package com.example.assetproject.service;

import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.History;
import com.example.assetproject.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<History> findAll() {
        return historyRepository.findAll();
    }

    public int countAllFiltered(String category, String keyword) {
        return historyRepository.countAllFiltered(category, "%" + (keyword == null ? "" : keyword) + "%");
    }

    public List<History> findAllWithPagingAndFilter(int pageNum, int pageSize, String category, String keyword, String sort, String order) {
        int offset = (pageNum - 1) * pageSize;
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        params.put("category", category);
        params.put("sort", sort);
        params.put("order", order);
        params.put("keyword", "%" + (keyword == null ? "" : keyword) + "%"); // 와일드카드 검색을 위해 키워드 양쪽에 % 추가
        return historyRepository.findAllWithPagingAndFilter(params);
    }
}
