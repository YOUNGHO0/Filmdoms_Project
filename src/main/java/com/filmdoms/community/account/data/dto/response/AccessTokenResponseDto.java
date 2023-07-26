package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.dto.LoginDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AccessTokenResponseDto {

    private String accessToken;
    private Long expiredAt;

    public static AccessTokenResponseDto from(LoginDto dto) {
        return AccessTokenResponseDto.builder()
                .accessToken(dto.getAccessToken())
                .expiredAt(dto.getExpiredAt())
                .build();
    }
}
