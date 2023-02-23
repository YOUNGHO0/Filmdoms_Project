package com.filmdoms.community.banner.data.dto;

import com.filmdoms.community.banner.data.entity.Banner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BannerDto {

    private Long id;
    private String imageUrl;
    private String title;

    public static BannerDto from(Banner entity) {
        return BannerDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getFirstImageUrl())
                .title(entity.getTitle())
                .build();
    }
}
