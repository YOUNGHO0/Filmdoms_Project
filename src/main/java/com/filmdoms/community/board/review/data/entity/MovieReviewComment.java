package com.filmdoms.community.board.review.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "\"movie_review_comment\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@BatchSize(size = 1000)
@SuperBuilder
public class MovieReviewComment extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "movie_review_header_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieReviewHeader header;

    @JoinColumn(name = "movie_review_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieReviewComment parentComment;

    private String content;

    @Builder.Default
    private CommentStatus status = CommentStatus.ACTIVE;


    public MovieReviewComment(MovieReviewHeader header, MovieReviewComment parentComment, String content) {
        this.header = header;
        this.parentComment = parentComment;
        this.content = content;
    }
}
