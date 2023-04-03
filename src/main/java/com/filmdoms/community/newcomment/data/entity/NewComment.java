package com.filmdoms.community.newcomment.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
        @Index(columnList = "article_id")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NewComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JoinColumn(name = "new_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private NewComment parentComment;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.ACTIVE;

    private int voteCount = 0;

    private boolean isManagerComment;

    @Builder
    private NewComment(Article article, NewComment parentComment, Account author, String content, boolean isManagerComment) {
        this.article = article;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
        this.isManagerComment = isManagerComment;
    }
}
