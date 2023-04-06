package com.filmdoms.community.banner.data.dto.response;

import com.filmdoms.community.banner.data.entity.Banner;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BannerResponseDto {

    private Long id;
    private String title;
    private FileResponseDto file;

    public static BannerResponseDto from(Banner banner) {
        return BannerResponseDto.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .file(FileResponseDto.from(banner.getFile()))
                .build();
    }
}
