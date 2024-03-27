package com.example.assetproject.repository;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.History;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    private final SqlSessionTemplate sql;

    public void save(History history) {
        sql.insert("History.save", history);
    }

}
