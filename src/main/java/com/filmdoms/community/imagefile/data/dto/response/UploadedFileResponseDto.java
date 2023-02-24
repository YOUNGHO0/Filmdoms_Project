package com.filmdoms.community.imagefile.data.dto.response;

import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UploadedFileResponseDto {

    String url;

    public static UploadedFileResponseDto from(UploadedFileDto dto) {
        return UploadedFileResponseDto.builder()
                .url(dto.getUrl())
                .build();
    }
}