package com.example.assetproject.repository;

import com.example.assetproject.entity.Admin;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final SqlSessionTemplate sql;

    public void save(Admin admin) {
        sql.insert("Admin.save", admin);
    }

    public boolean existsByUsername(String username) {
        // SELECT COUNT(*) 결과를 Integer로 받음
        Integer count = sql.selectOne("Admin.existsByUsername", username);
        // count > 0 이면 true, 그렇지 않으면 false 반환
        return count != null && count > 0;
    }

    public Admin findByUsername(String username) {
        return sql.selectOne("Admin.findByUsername", username);
    }

    public boolean existsByEmail(String email) {
        Integer count = sql.selectOne("Admin.existsByEmail", email);
        return count != null && count > 0;
    }

}
