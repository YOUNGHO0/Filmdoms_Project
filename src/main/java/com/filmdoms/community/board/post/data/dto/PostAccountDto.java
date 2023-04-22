package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.account.data.entity.Account;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class PostAccountDto {

    private Long id;
    private String nickname;
    private String email;

    public static PostAccountDto from(Account entity) {
        return new PostAccountDto(
                entity.getId(),
                entity.getNickname(),
                entity.getPassword()
        );
    }
}
