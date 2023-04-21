package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.AccountStatus;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AccountResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private AccountRole accountRole;
    private AccountStatus accountStatus;
    private boolean isSocialLogin;
    private FileResponseDto profileImage;

    public static AccountResponseDto from(Account entity) {
        return AccountResponseDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .accountRole(entity.getAccountRole())
                .accountStatus(entity.getAccountStatus())
                .isSocialLogin(entity.isSocialLogin())
                .profileImage(FileResponseDto.from(entity.getProfileImage()))
                .build();
    }
}
