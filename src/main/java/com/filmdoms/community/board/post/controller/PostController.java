package com.filmdoms.community.board.post.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.data.dto.response.PostCreateResponseDto;
import com.filmdoms.community.board.post.data.dto.response.PostMainPageResponseDto;
import com.filmdoms.community.board.post.data.dto.response.PostUpdateResponseDto;
import com.filmdoms.community.board.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/main-page")
    public Response<List<PostMainPageResponseDto>> viewMain() {
        return Response.success(postService.getMainPagePosts()
                .stream()
                .map(PostMainPageResponseDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public Response<PostCreateResponseDto> createPost(
            @AuthenticationPrincipal AccountDto accountDto,
            @RequestBody @Valid PostCreateRequestDto requestDto) {
        return Response.success(PostCreateResponseDto.from(
                postService.create(accountDto, requestDto)
        ));
    }

    @PutMapping("/{postId}")
    public Response updatePost(
            @AuthenticationPrincipal AccountDto accountDto,
            @PathVariable Long postId,
            @RequestBody @Valid PostUpdateRequestDto requestDto) {
        return Response.success(PostUpdateResponseDto.from(postService.update(accountDto, postId, requestDto)));
    }

    @DeleteMapping("/{postId}")
    public Response deletePost(
            @AuthenticationPrincipal AccountDto accountDto,
            @PathVariable Long postId) {
        postService.delete(accountDto, postId);
        return Response.success();
    }
}
