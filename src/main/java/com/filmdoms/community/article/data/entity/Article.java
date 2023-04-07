package com.filmdoms.community.article.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @JoinColumn(name = "content_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Content content;

    @Formula(("select count(1) from NEW_COMMENT c where c.ARTICLE_ID = id"))
    private int commentCount;

    @OneToMany(mappedBy = "article")
    private List<NewComment> comments;

    private int view = 0;

    private int voteCount = 0;

    private boolean containsImage = false;

    @Builder
    private Article(String title, Category category, Tag tag, Account author, String content) {
        this.title = title;
        this.category = category;
        this.tag = tag;
        this.author = author;
        this.content = new Content(content);
    }

    public static Article from(ArticleControllerToServiceDto dto)
    {
        return Article.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(dto.getAuthor())
                .category(dto.getCategory())
                .tag(dto.getTag())
                .build();
    }

    public int addVote() {
        return ++voteCount;
    }

    public int removeVote() {
        return --voteCount;
    }
}
