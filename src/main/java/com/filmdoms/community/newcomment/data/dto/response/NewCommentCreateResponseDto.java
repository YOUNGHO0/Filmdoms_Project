package com.filmdoms.community.newcomment.data.dto.response;

import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class NewCommentCreateResponseDto {

    private Long commentId;

    private NewCommentCreateResponseDto(NewComment comment) {
        this.commentId = comment.getId();
    }

    public static NewCommentCreateResponseDto from(NewComment comment) {
        return new NewCommentCreateResponseDto(comment);
    }
}
