package com.example.assetproject.controller;

import com.example.assetproject.entity.Asset;
import com.example.assetproject.entity.History;
import com.example.assetproject.entity.PaginationModel;
import com.example.assetproject.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/histories")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("")
    public String histories(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false, defaultValue = "asc") String order,
                            Model model) {

        // 페이지 번호가 1 미만인 경우 1로 강제 설정
        page = Math.max(page, 1);

        int pageSize = 10; // 페이지당 보여줄 아이템 수
        int totalItems = historyService.countAllFiltered(category, keyword);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize); // 전체 페이지 수 계산

        List<History> histories = historyService.findAllWithPagingAndFilter(page, pageSize, category, keyword, sort, order);
        PaginationModel pagination = new PaginationModel(page, totalPages, totalItems, pageSize);

        model.addAttribute("histories", histories);
        model.addAttribute("pagination", pagination); // 페이징 정보 모델 추가
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);

        return "asset/list/history";

    }



}
