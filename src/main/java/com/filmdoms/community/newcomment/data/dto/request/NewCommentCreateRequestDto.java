package com.filmdoms.community.newcomment.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewCommentCreateRequestDto {

    private Long articleId;

    private Long parentCommentId;

    private String content;

    private boolean isManagerComment;
}
