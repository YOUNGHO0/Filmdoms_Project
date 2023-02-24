package com.filmdoms.community.board.banner.data.dto.response;

import com.filmdoms.community.board.banner.data.dto.BannerDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BannerResponseDto {

    private String imageUrl;
    private String title;

    public static BannerResponseDto from(BannerDto dto) {
        return BannerResponseDto.builder()
                .imageUrl(dto.getImageUrl())
                .title(dto.getTitle())
                .build();
    }
}
