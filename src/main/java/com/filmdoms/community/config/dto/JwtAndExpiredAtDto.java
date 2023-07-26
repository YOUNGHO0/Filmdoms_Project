package com.filmdoms.community.config.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAndExpiredAtDto {
    private String jwtToken;
    private long expiredAt;
}
