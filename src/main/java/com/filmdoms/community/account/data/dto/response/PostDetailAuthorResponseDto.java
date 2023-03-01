package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;

@Getter
public class PostDetailAuthorResponseDto { //게시물 상세 조회 시 작성자 정보를 주는 DTO

    private Long id;
    private String username;

    public PostDetailAuthorResponseDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
    }
}
