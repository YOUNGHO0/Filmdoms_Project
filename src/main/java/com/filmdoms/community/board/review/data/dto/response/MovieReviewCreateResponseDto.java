package com.filmdoms.community.board.review.data.dto.response;

import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import lombok.Getter;

@Getter
public class MovieReviewCreateResponseDto {

    private Long postId;

    public MovieReviewCreateResponseDto(MovieReviewHeader header) {
        postId = header.getId();
    }
}
