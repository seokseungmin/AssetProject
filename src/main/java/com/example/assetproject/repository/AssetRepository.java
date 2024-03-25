package com.example.assetproject.repository;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.Hardware;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AssetRepository {

    private final SqlSessionTemplate sql;

//    public List<Asset> findAll() {
//        return sql.selectList("Asset.findAll");
//    }

    public void save(Asset asset) {
        sql.insert("Asset.saveHardwareAsset", asset);
    }

    public Asset findById(Long id) {
        return sql.selectOne("Asset.findById", id);
    }

    public void update(Asset asset) {
        sql.selectOne("Asset.update", asset);
    }

    public void deleteById(Long assetIdx) {
        sql.delete("Asset.deleteById", assetIdx);
    }

    public String findAssetCodeById(Long assetIdx) {
        return sql.selectOne("Asset.findAssetCodeById", assetIdx);
    }

//    public List<Asset> findAllWithPaging(Map<String, Object> params) {
//        return sql.selectList("Asset.findAllWithPaging", params);
//    }
//
//    public int countAll() {
//        return sql.selectOne("Asset.countAll");
//    }

    public List<Asset> findAllWithPagingAndFilter(Map<String, Object> params) {
        return sql.selectList("Asset.findAllWithPagingAndFilter", params);
    }

    public int countAllFiltered(String category, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("keyword", keyword);
        return sql.selectOne("Asset.countAllFiltered", params);
    }
}
