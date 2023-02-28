package com.filmdoms.community.board.banner.data.dto.response;

import com.filmdoms.community.board.banner.data.dto.BannerDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BannerInfoResponseDto {

    private Long id;
    private String imageUrl;
    private String title;

    public static BannerInfoResponseDto from(BannerDto dto) {
        return BannerInfoResponseDto.builder()
                .id(dto.getId())
                .imageUrl(dto.getImageUrl())
                .title(dto.getTitle())
                .build();
    }
}
