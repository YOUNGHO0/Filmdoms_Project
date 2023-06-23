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
        checkArticleExistAndAuthority(dto, accountDto); // 실제로 등록된 게시글이 있는지와, 요청한 유저의 권한을 확인합니다.

        return announceService.registerAnnounce(dto.getArticleId());
    }

    @DeleteMapping("/article/announce")
    public Response unregisterAnnounce(@RequestBody AnnounceRegisterDto dto, @AuthenticationPrincipal AccountDto accountDto) {
        checkArticleExistAndAuthority(dto, accountDto);

        return announceService.unregisterAnnounce(dto.getArticleId());

    }

    private void checkArticleExistAndAuthority(AnnounceRegisterDto stateChangeDto, AccountDto accountDto) {
        Optional<Article> optionalArticle = articleRepository.findById(stateChangeDto.getArticleId());
        if (optionalArticle.isEmpty())
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID);
        if (accountDto.getAccountRole() != AccountRole.ADMIN)
            throw new ApplicationException(ErrorCode.AUTHORIZATION_ERROR);
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
