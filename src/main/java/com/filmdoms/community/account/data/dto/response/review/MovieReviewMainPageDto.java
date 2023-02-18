package com.filmdoms.community.account.data.dto.response.review;

import com.filmdoms.community.account.data.entity.review.MovieReviewHeader;
import com.filmdoms.community.account.data.entity.review.MovieReviewTag;
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
