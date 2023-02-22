package com.filmdoms.community.board.post.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.review.data.entity.MovieReviewContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("PostHeader")
@Table(name = "\"post_header\"", indexes = {
        @Index(columnList = "category")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class PostHeader extends BoardHeadCore {

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @OrderBy("dateCreated DESC")
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<PostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "boardHeadCore", cascade = CascadeType.REMOVE)
    public final List<ImageFile> imageFiles = new ArrayList<>();

    @Builder
    private PostHeader(PostCategory category, String title, Account author, MovieReviewContent content) {
        super(title, author, content);
        this.category = category;
    }
}
