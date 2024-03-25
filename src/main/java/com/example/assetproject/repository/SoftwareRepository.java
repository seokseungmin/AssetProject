package com.example.assetproject.repository;

import com.example.assetproject.entity.Hardware;
import com.example.assetproject.entity.Software;
import com.example.assetproject.dto.SoftwareAssetDTO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SoftwareRepository {

    private final SqlSessionTemplate sql;

    public List<Software> findAll() {
        return sql.selectList("Software.findAll");
    }

    public void save(Software software) {
        sql.insert("Software.save", software);
    }

    public SoftwareAssetDTO findSoftwareById(Long softwareIdx) {
        return sql.selectOne("Software.findSoftwareById", softwareIdx);
    }

    public Software findById(Long softwareIdx) {
        return sql.selectOne("Software.findById", softwareIdx);
    }

    public void update(Software software) {
        sql.update("Software.update", software);
    }

    public void deleteById(Long softwareIdx) {
        sql.delete("Software.deleteSoftware", softwareIdx);
    }

    public Long findAssetIdxBySoftwareIdx(Long softwareIdx) {
        return sql.selectOne("Software.findAssetIdxBySoftwareIdx", softwareIdx);
    }

    public Long findSoftwareIdxByAssetIdx(Long assetIdx) {
        return sql.selectOne("Software.findSoftwareIdxByAssetIdx", assetIdx);
    }

//    public List<Software> findAllWithPaging(Map<String, Object> params) {
//        return sql.selectList("Software.findAllWithPaging", params);
//    }
//
//    public int countAll() {
//        return sql.selectOne("Software.countAll");
//    }

    public List<Software> findAllWithPagingAndFilter(Map<String, Object> params) {
        return sql.selectList("Software.findAllWithPagingAndFilter", params);
    }

    public int countAllFiltered(String category, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("keyword", keyword);
        return sql.selectOne("Software.countAllFiltered", params);
    }
}
