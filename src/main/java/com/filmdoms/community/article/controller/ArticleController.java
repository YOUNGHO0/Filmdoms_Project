package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.RecentListResponseDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.MovieAndRecentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.trending.TopFiveArticleResponseDto;
import com.filmdoms.community.article.service.ArticleService;
import com.filmdoms.community.article.service.InitService;
import com.filmdoms.community.article.service.InitService2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final InitService initService;
    private final InitService2 initService2;

    @PostMapping("/article")
    public Response<ArticleCreateResponseDto> createArticle(@RequestBody ParentCreateRequestDto parentCreateRequestDto, @AuthenticationPrincipal AccountDto accountDto) {
        ArticleCreateResponseDto responseDto = articleService.createArticle(parentCreateRequestDto, accountDto);
        return Response.success(responseDto);
    }

    @GetMapping("/main/{category}")
    public Response<List<? extends ParentMainPageResponseDto>> readMain(@PathVariable Category category, @RequestParam(defaultValue = "5") int limit) {
        List<? extends ParentMainPageResponseDto> dtoList = articleService.getMainPageDtoList(category, limit);
        return Response.success(dtoList);
    }

    @GetMapping("/main/recent")
    public Response<List<MovieAndRecentMainPageResponseDto>> readMain(@RequestParam(defaultValue = "5") int limit) {
        List<MovieAndRecentMainPageResponseDto> dtoList = articleService.getRecentMainPageDtoList(limit);
        return Response.success(dtoList);
    }

    @GetMapping("/article/{category}/{articleId}")
    public Response<ArticleDetailResponseDto> readDetail(@PathVariable Category category, @PathVariable Long articleId, @AuthenticationPrincipal AccountDto accountDto) {
        ArticleDetailResponseDto dto = articleService.getDetail(category, articleId, accountDto);
        return Response.success(dto);
    }

    @GetMapping("/article/init-data")
    public Response initData(@RequestParam(defaultValue = "10") int limit) {
        initService.makeArticleData(limit);
        initService2.initData();
        return Response.success();
    }

    @GetMapping("/article/recent")
    public Response getRecentArticles(@RequestParam(required = false) Tag tag, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (pageable.getPageSize() > 50)
            pageable = PageRequest.of(pageable.getPageNumber(), 24, Sort.by(Sort.Direction.DESC, "id")); //Article의 id로 역정렬
        Page<RecentListResponseDto> recentArticles = articleService.getRecentArticles(tag, pageable);

        if (recentArticles.getTotalPages() - 1 < pageable.getPageNumber())
            return Response.error(ErrorCode.INVALID_PAGE_NUMBER.getMessage());

        return Response.success(recentArticles);


    }


    @GetMapping("/article/{category}")
    public Response getBoardCategoryList(@PathVariable Category category, @RequestParam(required = false) Tag tag, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        if (pageable.getPageSize() > 50)
            pageable = PageRequest.of(pageable.getPageNumber(), 24, Sort.by(Sort.Direction.DESC, "id")); //Article의 id로 역정렬

        Page<? extends ParentBoardListResponseDto> boardList = articleService.getBoardList(category, tag, pageable);

        if (boardList == null)
            return Response.error(ErrorCode.CATEGORY_NOT_FOUND.getMessage());

        if (boardList.getTotalPages() - 1 < pageable.getPageNumber())
            return Response.error(ErrorCode.INVALID_PAGE_NUMBER.getMessage());

        return Response.success(boardList);
    }

    @GetMapping("/article/top-posts")
    public Response getTopPosts() {
        List<TopFiveArticleResponseDto> topFiveArticles = articleService.getTopFiveArticles();
        return Response.success(topFiveArticles);
    }
}
