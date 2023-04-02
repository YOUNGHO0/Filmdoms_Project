package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.response.MainPageResponseDto;
import com.filmdoms.community.article.service.ArticleService;
import com.filmdoms.community.article.service.InitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController2 {

    private final ArticleService articleService;
    private final InitService initService;

    @GetMapping("/{category}/main")
    public Response<List<? extends MainPageResponseDto>> readMain(@PathVariable Category category, @RequestParam(defaultValue = "5") int limit) {
        List<? extends MainPageResponseDto> dtoList = articleService.getMainPageDtoList(category, limit);
        return Response.success(dtoList);
    }

    @GetMapping("/recent/main")
    public Response readMain(@RequestParam(defaultValue = "5") int limit) {
        return Response.success();
    }

    @GetMapping("/init-data")
    public Response initData(@RequestParam(defaultValue = "10") int limit) {
        initService.makeArticleData(limit);
        return Response.success();
    }
}