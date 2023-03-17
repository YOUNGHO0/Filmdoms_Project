package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.constants.OAuthType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OAuthResponseDto {

    private OAuthType type;
    private String accessToken;
}
