package com.example.assetproject.repository;

import com.example.assetproject.dto.Software;
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
}
