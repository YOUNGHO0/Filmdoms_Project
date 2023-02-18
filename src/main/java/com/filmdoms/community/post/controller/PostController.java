package com.filmdoms.community.post.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.post.data.dto.response.PostMainPageResponseDto;
import com.filmdoms.community.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/main/post")
    public Response<List<PostMainPageResponseDto>> viewMain() {
        return Response.success(postService.getMainPagePosts()
                .stream()
                .map(PostMainPageResponseDto::from)
                .collect(Collectors.toList()));
    }
}
