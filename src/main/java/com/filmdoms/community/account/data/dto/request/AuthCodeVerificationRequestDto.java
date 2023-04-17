package com.filmdoms.community.account.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthCodeVerificationRequestDto {

    private String email;
    private String authCode;
}
