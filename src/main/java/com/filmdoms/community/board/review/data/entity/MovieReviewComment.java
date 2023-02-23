package com.filmdoms.community.board.review.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "movie_review_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@BatchSize(size = 1000)
@Getter
public class MovieReviewComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "movie_review_header_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieReviewHeader header;

    @JoinColumn(name = "movie_review_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieReviewComment parentComment;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    private String content;


    private CommentStatus status = CommentStatus.ACTIVE;

    @Builder
    private MovieReviewComment(MovieReviewHeader header, MovieReviewComment parentComment, Account author, String content) {
        this.header = header;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
    }
}
