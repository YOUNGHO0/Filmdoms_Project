package com.filmdoms.community.postComment.data.dto;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.postComment.data.entity.PostComment;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentDto {

    private Long id;
    private Long postId;
    private AccountDto account;
    private String content;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public static PostCommentDto from(PostComment entity) {
        return new PostCommentDto(
                entity.getId(),
                entity.getPost().getId(),
                AccountDto.from(entity.getAccount()),
                entity.getContent(),
                entity.getDateCreated(),
                entity.getDateLastModified()
        );
    }

}
