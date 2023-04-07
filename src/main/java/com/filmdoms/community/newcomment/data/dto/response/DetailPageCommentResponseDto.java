package com.filmdoms.community.newcomment.data.dto.response;

import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailPageCommentResponseDto {

    private int commentCount;

    private List<ParentNewCommentResponseDto> comments;

    private DetailPageCommentResponseDto(List<NewComment> comments) {
        this.commentCount = comments.size();
        this.comments = ParentNewCommentResponseDto.convert(comments);
    }

    public static DetailPageCommentResponseDto from(List<NewComment> comments) {
        return new DetailPageCommentResponseDto(comments);
    }
}
