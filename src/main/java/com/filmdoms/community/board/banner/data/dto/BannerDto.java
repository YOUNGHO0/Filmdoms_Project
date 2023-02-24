package com.filmdoms.community.board.banner.data.dto;

import com.filmdoms.community.board.banner.data.entity.BannerHeader;
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

    public static BannerDto from(BannerHeader entity, String domain) {
        return BannerDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getFirstImageUrl(domain))
                .title(entity.getTitle())
                .build();
    }
}
