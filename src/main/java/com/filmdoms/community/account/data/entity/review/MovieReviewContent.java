package com.filmdoms.community.account.data.entity.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"movie_review_content\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieReviewContent {

    @Id @GeneratedValue
    private Long id;

    private String content;

    public MovieReviewContent(String content) {
        this.content = content;
    }
}
