package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.SearchMethod;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{category}/{searchMethod}")
public class ArticleSearchController {

    private final ArticleService articleService;

    @GetMapping
    public Response getSearchRequest(@PathVariable Category category, @PathVariable SearchMethod searchMethod, @RequestParam String keyword,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (pageable.getPageSize() > 50)
            pageable = PageRequest.of(pageable.getPageNumber(), 24, Sort.by(Sort.Direction.DESC, "id"));

        Page<? extends ParentBoardListResponseDto> responseDtos;
        switch (searchMethod) {
            case TITLE_CONTENT:
                responseDtos = articleService.findArticlesByKeyword(category, keyword, pageable);
                return Response.success(responseDtos);
            case NICKNAME:
                responseDtos = articleService.findArticlesByNickname(category, keyword, pageable);
                return Response.success(responseDtos);
        }

        return Response.error("정상적이지 않은 접근입니다");
    }
}
