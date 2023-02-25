package com.filmdoms.community.board.post.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.response.PostCreateResponseDto;
import com.filmdoms.community.board.post.data.dto.response.PostMainPageResponseDto;
import com.filmdoms.community.board.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/create")
    public Response create(
            @AuthenticationPrincipal AccountDto accountDto,
            @RequestPart(value = "data") @Valid PostCreateRequestDto requestDto,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImageFile,
            @RequestPart(value = "subImage", required = false) List<MultipartFile> subImageFiles) {
        return Response.success(PostCreateResponseDto.from(
                postService.create(accountDto, requestDto, mainImageFile, subImageFiles)
        ));

    }
}
