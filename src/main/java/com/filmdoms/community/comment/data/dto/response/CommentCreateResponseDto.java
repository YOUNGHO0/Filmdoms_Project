package com.filmdoms.community.comment.data.dto.response;

import com.filmdoms.community.comment.data.entity.Comment;
import lombok.Getter;

@Getter
public class CommentCreateResponseDto {

    private Long commentId;

    private CommentCreateResponseDto(Comment comment) {
        this.commentId = comment.getId();
    }

    public static CommentCreateResponseDto from(Comment comment) {
        return new CommentCreateResponseDto(comment);
    }
}
