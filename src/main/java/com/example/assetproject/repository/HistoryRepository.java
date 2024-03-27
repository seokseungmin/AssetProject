package com.example.assetproject.repository;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.History;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    private final SqlSessionTemplate sql;

    public void save(History history) {
        sql.insert("History.save", history);
    }

    public List<History> findAll() {
        return sql.selectList("History.findAll");
    }

    public int countAllFiltered(String category, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("keyword", keyword);
        return sql.selectOne("History.countAllFiltered", params);
    }

    public List<History> findAllWithPagingAndFilter(Map<String, Object> params) {
        return sql.selectList("History.findAllWithPagingAndFilter", params);
    }
}
