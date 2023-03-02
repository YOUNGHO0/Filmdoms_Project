package com.filmdoms.community.board.comment.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment", indexes = {
        @Index(columnList = "board_head_core_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @JoinColumn(name = "board_head_core_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardHeadCore header;

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

    @Builder
    private Comment(BoardHeadCore header, Comment parentComment, Account author, String content) {
        this.header = header;
        this.parentComment = parentComment;
        this.author = author;
        this.content = content;
    }
}
