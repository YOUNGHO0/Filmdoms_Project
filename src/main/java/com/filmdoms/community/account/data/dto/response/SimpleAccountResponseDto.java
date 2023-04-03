package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;

@Getter
public class SimpleAccountResponseDto {

    private Long id;
    private String username;

    public SimpleAccountResponseDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
    }

    private SimpleAccountResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static SimpleAccountResponseDto from(Account account) {
        return new SimpleAccountResponseDto(account.getId(), account.getUsername());
    }
}
