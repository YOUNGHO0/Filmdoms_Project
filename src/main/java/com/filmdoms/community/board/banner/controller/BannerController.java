package com.filmdoms.community.board.banner.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.banner.data.dto.request.BannerInfoRequestDto;
import com.filmdoms.community.board.banner.data.dto.response.BannerInfoResponseDto;
import com.filmdoms.community.board.banner.data.dto.response.BannerMainPageGetResponseDto;
import com.filmdoms.community.board.banner.service.BannerService;
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
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/main-page")
    public Response<List<BannerMainPageGetResponseDto>> getMainPageBanner() {
        return Response.success(bannerService.getMainPageBanner()
                .stream()
                .map(BannerMainPageGetResponseDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping()
    public Response<BannerInfoResponseDto> createBanner(
            @AuthenticationPrincipal AccountDto accountDto,
            @RequestBody BannerInfoRequestDto requestDto) {
        return Response.success(BannerInfoResponseDto.from(bannerService.create(accountDto, requestDto)));
    }

    @PutMapping("/{bannerId}")
    public Response<BannerInfoResponseDto> updateBanner(
            @PathVariable Long bannerId,
            @RequestBody BannerInfoRequestDto requestDto) {
        return Response.success(BannerInfoResponseDto.from(bannerService.update(bannerId, requestDto)));
    }

    @DeleteMapping("/{bannerId}")
    public Response deleteBanner(@PathVariable Long bannerId) {
        bannerService.delete(bannerId);
        return Response.success();
    }
}
