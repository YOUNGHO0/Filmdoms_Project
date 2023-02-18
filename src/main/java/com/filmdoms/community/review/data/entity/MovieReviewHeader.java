package com.filmdoms.community.review.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.review.data.constant.MovieReviewTag;
import com.filmdoms.community.review.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"movie_review_header\"")
@Getter
public class MovieReviewHeader extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private MovieReviewTag tag;

    private String title;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    private int view = 0;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE)
    private List<MovieReviewComment> comments = new ArrayList<>();

    @JoinColumn(name = "movie_review_content_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MovieReviewContent content;

    @Builder
    public MovieReviewHeader(MovieReviewTag tag, String title, Account author, MovieReviewContent content) {
        this.tag = tag;
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
