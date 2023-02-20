package com.filmdoms.community.board.review.data.dto.response;

import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import lombok.Getter;

@Getter
public class MovieReviewMainPageDto {

    private MovieReviewTag tag;
    private String title;
    private int commentNum;

    public MovieReviewMainPageDto(MovieReviewHeader header) {
        this.tag = header.getTag();
        this.title = header.getTitle();
        this.commentNum = header.getComments().size();
    }
}
