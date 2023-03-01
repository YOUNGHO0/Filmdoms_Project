package com.filmdoms.community.board.comment.data.dto;

import com.filmdoms.community.board.comment.data.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class ParentCommentResponseDto extends CommentResponseDto {

    private List<CommentResponseDto> childComments;

    public ParentCommentResponseDto(Comment comment, List<CommentResponseDto> childComments) {
        super(comment);
        this.childComments = childComments;
    }
}
