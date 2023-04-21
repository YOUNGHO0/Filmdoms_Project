package com.filmdoms.community.comment.data.dto.response;

import com.filmdoms.community.comment.data.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailPageCommentResponseDto {

    private int commentCount;

    private List<ParentCommentResponseDto> comments;

    private DetailPageCommentResponseDto(List<Comment> comments) {
        this.commentCount = comments.size();
        this.comments = ParentCommentResponseDto.convert(comments);
    }

    public static DetailPageCommentResponseDto from(List<Comment> comments) {
        return new DetailPageCommentResponseDto(comments);
    }
}
