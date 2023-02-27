package com.filmdoms.community.board.review.data.dto.request;

import com.filmdoms.community.board.data.constant.MovieReviewTag;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor //테스트에 필요
@NoArgsConstructor
@Getter
public class MovieReviewCreateRequestDto {

    private MovieReviewTag tag;
    private String title;
    private String content;
    private Long mainImageId;
    private Set<Long> contentImageId;
}
