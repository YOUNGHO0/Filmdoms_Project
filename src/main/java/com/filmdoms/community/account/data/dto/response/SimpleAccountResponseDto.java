package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;

@Getter
public class SimpleAccountResponseDto {

    private Long id;
    private String nickname;

    public SimpleAccountResponseDto(Account account) { //리팩토링 후 삭제 예정
        this.id = account.getId();
        this.nickname = account.getNickname();
    }

    private SimpleAccountResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static SimpleAccountResponseDto from(Account account) {
        return new SimpleAccountResponseDto(account.getId(), account.getNickname());
    }
}
