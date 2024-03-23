package com.example.assetproject.repository;

import com.example.assetproject.dto.Hardware;
import com.example.assetproject.dto.HardwareAssetDTO;
import com.example.assetproject.form.AssetHardwareAddForm;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HardwareRepository {

    private final SqlSessionTemplate sql;

    public List<Hardware> findAll() {
        return sql.selectList("Hardware.findAll");
    }

    // AssetHardwareAddForm 데이터를 받아서 데이터베이스에 저장
    public void save(Hardware hardware) {
        sql.insert("Hardware.save", hardware);
    }

    public Hardware findById(Long hardwareIdx) {
        return sql.selectOne("Hardware.findById", hardwareIdx);
    }

    public void update(Hardware hardware) {
        sql.update("Hardware.update", hardware);
    }

    public HardwareAssetDTO findHardwareById(Long hardwareIdx) {
        return sql.selectOne("Hardware.findHardwareById", hardwareIdx);
    }
}
