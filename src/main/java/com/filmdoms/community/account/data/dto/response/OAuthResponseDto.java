package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.constant.OAuthType;
import lombok.Getter;

@Getter
public class OAuthResponseDto {

    private OAuthType type;
    private String accessToken;

    private OAuthResponseDto(OAuthType type, String accessToken) {
        this.type = type;
        this.accessToken = accessToken;
    }

    public static OAuthResponseDto from(OAuthType type, String accessToken) {
        return new OAuthResponseDto(type, accessToken);
    }
}
