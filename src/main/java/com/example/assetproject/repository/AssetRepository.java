package com.example.assetproject.repository;

import com.example.assetproject.dto.Asset;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AssetRepository {

    private final SqlSessionTemplate sql;

    public List<Asset> findAll() {
        return sql.selectList("Asset.findAll");
    }

    public void save(Asset asset) {
        sql.insert("Asset.saveHardwareAsset", asset);
    }

    public Asset findById(Long id) {
        return sql.selectOne("Asset.findById", id);
    }

    public void update(Asset asset) {
        sql.selectOne("Asset.update", asset);
    }
}
