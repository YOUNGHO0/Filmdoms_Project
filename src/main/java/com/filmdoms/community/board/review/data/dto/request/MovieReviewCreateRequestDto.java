package com.filmdoms.community.board.review.data.dto.request;

import com.filmdoms.community.board.data.constant.MovieReviewTag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class MovieReviewCreateRequestDto {

    private MovieReviewTag tag;
    private String title;
    private String content;
    private List<Long> imageIds;
}
