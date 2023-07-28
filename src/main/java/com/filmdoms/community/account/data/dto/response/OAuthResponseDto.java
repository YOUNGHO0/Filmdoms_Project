package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.constant.OAuthType;
import com.filmdoms.community.config.dto.JwtAndExpiredAtDto;
import lombok.Getter;

@Getter
public class OAuthResponseDto {

    private OAuthType type;
    private String accessToken;
    private long expiredAt;

    private OAuthResponseDto(OAuthType type, JwtAndExpiredAtDto jwtAndExpiredAtDto) {
        this.type = type;
        this.accessToken = jwtAndExpiredAtDto.getJwtToken();
        this.expiredAt = jwtAndExpiredAtDto.getExpiredAt();
    }

    public static OAuthResponseDto from(OAuthType type, JwtAndExpiredAtDto jwtAndExpiredAtDto) {
        return new OAuthResponseDto(type, jwtAndExpiredAtDto);
    }
}
