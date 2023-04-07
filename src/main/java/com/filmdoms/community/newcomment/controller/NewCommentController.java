package com.filmdoms.community.newcomment.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.newcomment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.newcomment.service.NewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewCommentController {

    private final NewCommentService newCommentService;

    @GetMapping("/article/{category}/{articleId}/comments")
    public Response<DetailPageCommentResponseDto> readArticleComments(@PathVariable Long articleId, @PathVariable Category category) {
        DetailPageCommentResponseDto dto = newCommentService.getDetailPageCommentList(articleId);
        return Response.success(dto);
    }
}
