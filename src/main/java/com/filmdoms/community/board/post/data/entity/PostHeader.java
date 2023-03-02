package com.filmdoms.community.board.post.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PostHeader")
@Table(name = "post_header", indexes = {
        @Index(columnList = "category")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHeader extends BoardHeadCore {

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @OrderBy("dateCreated DESC")
    @OneToMany(mappedBy = "header", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<PostComment> comments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "image_file_id")
    private ImageFile mainImage;

    @Builder
    private PostHeader(PostCategory category, String title, Account author, BoardContent content, ImageFile mainImage) {
        super(title, author, content);
        this.category = category;
        this.mainImage = mainImage;
    }

    public void update(PostCategory category, String title, String content, ImageFile mainImage) {
        updateBoardHeadCore(title, content);
        this.category = category;
        this.mainImage = mainImage;
    }
}
