package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import lombok.Getter;

@Getter
public class CommentAuthorResponseDto { //댓글 작성자 정보를 주는 DTO

    private Long id;
    private String username;

    public CommentAuthorResponseDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
    }
}
