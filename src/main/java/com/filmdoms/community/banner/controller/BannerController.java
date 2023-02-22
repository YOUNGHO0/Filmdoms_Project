package com.filmdoms.community.banner.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.banner.data.dto.response.BannerResponseDto;
import com.filmdoms.community.banner.service.BannerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/main-page")
    public Response<List<BannerResponseDto>> getMainPageBanner() {
        return Response.success(bannerService.getMainPageBanner()
                .stream()
                .map(BannerResponseDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/init-data")
    public Response<Void> setInitData(
            @RequestParam(required = false) String title,
            @RequestParam(value = "image", required = false) MultipartFile multipartFile
    ) {
        bannerService.setInitData(title, multipartFile);
        return Response.success();
    }
}
