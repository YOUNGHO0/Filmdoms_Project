package com.filmdoms.community.account.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginDto {
    private long expiredAt;
    private String accessToken;
    private String refreshToken;
}
