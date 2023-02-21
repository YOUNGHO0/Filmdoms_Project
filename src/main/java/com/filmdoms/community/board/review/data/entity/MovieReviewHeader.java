package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"movie_review_header\"")

public class MovieReviewHeader extends BoardHeadCore {

    @Enumerated(EnumType.STRING)
    private MovieReviewTag tag;

    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE)
    private List<MovieReviewComment> comments = new ArrayList<>();

    @Builder
    public MovieReviewHeader(String title, Account author, int view, PostStatus postStatus, BoardContent content, String preHeader,MovieReviewTag tag)
    {
        super(title,author,view,postStatus,content);
        this.tag = tag;

    }

}
