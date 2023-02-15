package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private String username;
    private String nickname;
    private String email;

    public static LoginResponseDto from(AccountDto dto) {
        return new LoginResponseDto(
                dto.getUsername(),
                dto.getNickname(),
                dto.getEmail()
        );
    }
}
