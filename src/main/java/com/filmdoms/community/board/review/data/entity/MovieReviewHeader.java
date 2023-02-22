package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_review_header")
@DiscriminatorValue("MovieReviewHeader")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MovieReviewHeader extends BoardHeadCore {

    @Enumerated(EnumType.STRING)
    private MovieReviewTag tag;

    @OneToMany(mappedBy = "header", cascade = CascadeType.REMOVE)
    private List<MovieReviewComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "boardHeadCore")
    private List<ImageFile> imageFiles = new ArrayList<>();

    @Builder
    private MovieReviewHeader(MovieReviewTag tag, String title, Account author, BoardContent boardContent) {
        super(title, author, boardContent);
        this.tag = tag;
    }
}
