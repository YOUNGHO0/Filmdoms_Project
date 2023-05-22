package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.response.boardlist.AnnounceListResponseDto;
import com.filmdoms.community.article.service.AnnounceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnnounceController {

    private final AnnounceService announceService;


    @GetMapping("/article/announce")
    public Response getAllAnnounceArticles(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AnnounceListResponseDto> announceArticles = announceService.getAllAnnounceArticles(pageable);
        return Response.success(announceArticles);
    }

    @GetMapping("/article/{category}/announce")
    public Response getAnnounceArticlesByCategory(@PathVariable Category category, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AnnounceListResponseDto> announceArticles = announceService.getAnnounceArticlesByCategory(category, pageable);
        return Response.success(announceArticles);
    }
}
