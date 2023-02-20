package com.filmdoms.community.postComment.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.postComment.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    // TODO: 댓글 작성 기능 구현 후 삭제
    @PostMapping("/test/comment")
    public Response testComment(@RequestParam Long postId) {
        postCommentService.testComment(postId);
        return Response.success();
    }
}
