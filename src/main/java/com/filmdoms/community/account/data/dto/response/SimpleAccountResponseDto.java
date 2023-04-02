package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;

@Getter
public class SimpleAccountResponseDto { //게시물 상세 조회 시 작성자 정보를 주는 DTO

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
