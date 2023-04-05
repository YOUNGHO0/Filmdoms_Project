package com.filmdoms.community.banner.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.banner.data.dto.request.BannerRequestDto;
import com.filmdoms.community.banner.service.BannerService;
import com.filmdoms.community.banner.data.dto.response.BannerResponseDto;
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
@RequestMapping("/api/v1/article/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/main")
    public Response<List<BannerResponseDto>> getMainPageBanner() {
        return Response.success(bannerService.getMainPageBanner());
    }

    @PostMapping()
    public Response<BannerResponseDto> createBanner(
            @AuthenticationPrincipal AccountDto accountDto,
            @RequestBody BannerRequestDto requestDto) {
        return Response.success(bannerService.create(accountDto, requestDto));
    }

    @PutMapping("/{bannerId}")
    public Response<BannerResponseDto> updateBanner(
            @PathVariable Long bannerId,
            @RequestBody BannerRequestDto requestDto) {
        return Response.success(bannerService.update(bannerId, requestDto));
    }

    @DeleteMapping("/{bannerId}")
    public Response deleteBanner(@PathVariable Long bannerId) {
        bannerService.delete(bannerId);
        return Response.success();
    }
}
