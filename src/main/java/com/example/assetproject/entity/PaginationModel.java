package com.example.assetproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationModel {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;

    // 생성자, 게터, 세터 ...
}
