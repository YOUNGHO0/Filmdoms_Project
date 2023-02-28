package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.account.data.entity.Account;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostAccountDto that = (PostAccountDto) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username)
                && Objects.equals(email, that.email);
    }
}
