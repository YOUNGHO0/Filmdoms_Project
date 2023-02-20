package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"movie_review_header\"")
@Data
public class MovieReviewHeader extends BoardHeadCore {

    @Enumerated(EnumType.STRING)
    private MovieReviewTag tag;

    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE)
    private List<MovieReviewComment> comments = new ArrayList<>();

    @Builder
    public MovieReviewHeader(MovieReviewTag tag, String title, Account author, MovieReviewContent content) {
        this.tag = tag;
        this.setTitle(title);
        this.setAuthor(author);
        this.setContent(content);
    }

}
