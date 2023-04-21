package com.filmdoms.community.comment.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.comment.data.dto.request.CommentCreateRequestDto;
import com.filmdoms.community.comment.data.dto.request.CommentUpdateRequestDto;
import com.filmdoms.community.comment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.comment.data.dto.response.CommentCreateResponseDto;
import com.filmdoms.community.comment.data.dto.response.CommentVoteResponseDto;
import com.filmdoms.community.comment.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/article/{category}/{articleId}/comment")
    public Response<DetailPageCommentResponseDto> read(@PathVariable Category category, @PathVariable Long articleId) {
        DetailPageCommentResponseDto dto = commentService.getDetailPageCommentList(articleId);
        return Response.success(dto);
    }

    @PostMapping("/comment")
    public Response<CommentCreateResponseDto> create(@RequestBody CommentCreateRequestDto requestDto,
                                                     @AuthenticationPrincipal AccountDto accountDto) {
        CommentCreateResponseDto responseDto = commentService.createComment(requestDto, accountDto);
        return Response.success(responseDto);
    }

    @PostMapping("/comment/{commentId}/vote")
    public Response<CommentVoteResponseDto> vote(@PathVariable Long commentId,
                                                 @AuthenticationPrincipal AccountDto accountDto) {
        return Response.success(commentService.toggleCommentVote(commentId, accountDto));
    }

    @PutMapping("/comment/{commentId}")
    public Response update(@RequestBody CommentUpdateRequestDto requestDto, @PathVariable Long commentId,
                           @AuthenticationPrincipal AccountDto accountDto) {
        commentService.updateComment(requestDto, commentId, accountDto);
        return Response.success();
    }

    @DeleteMapping("/comment/{commentId}")
    public Response delete(@PathVariable Long commentId, @AuthenticationPrincipal AccountDto accountDto) {
        commentService.deleteComment(commentId, accountDto);
        return Response.success();
    }
}
