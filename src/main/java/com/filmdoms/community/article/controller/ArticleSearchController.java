package com.filmdoms.community.article.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
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
@RequestMapping("/api/v1/{category}/search")
public class ArticleSearchController {

    private final ArticleService articleService;

    @GetMapping
    public Response getSearchRequest(@PathVariable Category category, @RequestParam(required = false) String keyword, @RequestParam(required = false) String nickname,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (pageable.getPageSize() > 50)
            pageable = PageRequest.of(pageable.getPageNumber(), 24, Sort.by(Sort.Direction.DESC, "id"));

        log.info("키워드 {}, 인포 {}", keyword, nickname);
        if (keyword == null && nickname == null)
            return Response.error(ErrorCode.NO_KEYWORD_FOUND.getMessage());
        if (keyword != null && nickname != null)
            return Response.error(ErrorCode.TOO_MANY_KEYWORD.getMessage());

        if (keyword != null) {
            Page<? extends ParentBoardListResponseDto> responseDtos = articleService.findArticlesByKeyword(category, keyword, pageable);
            return Response.success(responseDtos);
        }
        if (nickname != null) {
            Page<? extends ParentBoardListResponseDto> responseDtos = articleService.findArticlesByNickname(category, nickname, pageable);
            return Response.success(responseDtos);
        }
        return Response.error("정상적이지 않은 접근입니다");
    }
}
