package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.board.post.data.entity.PostComment;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentDto {

    private Long id;
    private Long postId;
    private PostAccountDto author;
    private String content;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;

    public static PostCommentDto from(PostComment entity) {
        return new PostCommentDto(
                entity.getId(),
                entity.getHeader().getId(),
                PostAccountDto.from(entity.getAuthor()),
                entity.getContent(),
                entity.getDateCreated(),
                entity.getDateLastModified()
        );
    }

}
