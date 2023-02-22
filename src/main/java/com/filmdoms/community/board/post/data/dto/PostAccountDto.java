package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.account.data.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostAccountDto {


    private Long id;
    private String username;
    private String nickname;
    private String email;

    public static PostAccountDto from(Account entity) {
        return new PostAccountDto(
                entity.getId(),
                entity.getUsername(),
                entity.getNickname(),
                entity.getPassword()
        );
    }
}
