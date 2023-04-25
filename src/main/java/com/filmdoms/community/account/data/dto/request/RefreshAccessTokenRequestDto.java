package com.filmdoms.community.account.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshAccessTokenRequestDto {

    private String refreshToken;
}
