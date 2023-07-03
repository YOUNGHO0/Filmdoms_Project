package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.request.announce.AnnounceRegisterDto;
import com.filmdoms.community.article.data.dto.response.boardlist.AnnounceListResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.service.AnnounceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnnounceController {

    private final AnnounceService announceService;
    private final ArticleRepository articleRepository;


    @PostMapping("/article/announce")
    public Response registerAnnounce(@RequestBody AnnounceRegisterDto dto, @AuthenticationPrincipal AccountDto accountDto) {
        return announceService.registerAnnounce(dto.getArticleId());
    }

    @DeleteMapping("/article/announce")
    public Response unregisterAnnounce(@RequestBody AnnounceRegisterDto dto, @AuthenticationPrincipal AccountDto accountDto) {
        return announceService.unregisterAnnounce(dto.getArticleId());
    }


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
