package com.filmdoms.community.newcomment.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.newcomment.data.dto.request.NewCommentCreateRequestDto;
import com.filmdoms.community.newcomment.data.dto.request.NewCommentUpdateRequestDto;
import com.filmdoms.community.newcomment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.newcomment.data.dto.response.NewCommentCreateResponseDto;
import com.filmdoms.community.newcomment.data.dto.response.NewCommentVoteResponseDto;
import com.filmdoms.community.newcomment.service.NewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewCommentController {

    private final NewCommentService newCommentService;

    @GetMapping("/article/{category}/{articleId}/comment")
    public Response<DetailPageCommentResponseDto> read(@PathVariable Category category, @PathVariable Long articleId) {
        DetailPageCommentResponseDto dto = newCommentService.getDetailPageCommentList(articleId);
        return Response.success(dto);
    }

    @PostMapping("/comment")
    public Response<NewCommentCreateResponseDto> create(@RequestBody NewCommentCreateRequestDto requestDto,
                                                        @AuthenticationPrincipal AccountDto accountDto) {
        NewCommentCreateResponseDto responseDto = newCommentService.createComment(requestDto, accountDto);
        return Response.success(responseDto);
    }

    @PostMapping("/comment/{commentId}/vote")
    public Response<NewCommentVoteResponseDto> vote(@PathVariable Long commentId,
                                                    @AuthenticationPrincipal AccountDto accountDto) {
        return Response.success(newCommentService.toggleCommentVote(commentId, accountDto));
    }

    @PutMapping("/comment/{commentId}")
    public Response update(@RequestBody NewCommentUpdateRequestDto requestDto, @PathVariable Long commentId,
                           @AuthenticationPrincipal AccountDto accountDto) {
        newCommentService.updateComment(requestDto, commentId, accountDto);
        return Response.success();
    }

    @DeleteMapping("/comment/{commentId}")
    public Response delete(@PathVariable Long commentId, @AuthenticationPrincipal AccountDto accountDto) {
        newCommentService.deleteComment(commentId, accountDto);
        return Response.success();
    }
}
