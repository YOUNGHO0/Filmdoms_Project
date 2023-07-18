package com.filmdoms.community.comment.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.account.data.entity.BaseTimeEntity;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JoinColumn(name = "comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

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
    private Comment(Article article, Comment parentComment, Account author, String content,
                    boolean isManagerComment) {
        this.article = article;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
        this.isManagerComment = isManagerComment;
    }

    public void update(String content) {
        this.content = content;
    }

    public void changeStatusToDeleted() {
        this.status = CommentStatus.DELETED;
    }

    public void changeStatusToActive() { this.status = CommentStatus.ACTIVE; }

    public int removeVote() {
        return --voteCount;
    }

    public int addVote() {
        return ++voteCount;
    }
}
