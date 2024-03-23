package com.example.assetproject.repository;

import com.example.assetproject.dto.Software;
import com.example.assetproject.dto.SoftwareAssetDTO;
import com.example.assetproject.form.AssetSoftwareUpdateForm;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
