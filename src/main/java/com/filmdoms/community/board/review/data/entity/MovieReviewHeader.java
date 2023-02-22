package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "\"movie_review_header\"")

public class MovieReviewHeader extends BoardHeadCore {

    @Enumerated(EnumType.STRING)
    private MovieReviewTag tag;


    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE)
    private List<MovieReviewComment> comments = new ArrayList<>();


    @Builder
    public MovieReviewHeader(String title, Account author, BoardContent content, MovieReviewTag tag)
    {
        super(title,author,content);
        this.tag = tag;

    }

}
