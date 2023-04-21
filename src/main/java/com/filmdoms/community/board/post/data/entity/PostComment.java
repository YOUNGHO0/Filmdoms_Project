package com.filmdoms.community.board.post.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.BaseTimeEntity;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Account author;

    @JsonIgnore
    @JoinColumn(name = "post_header_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PostHeader header;

    @JoinColumn(name = "post_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment parentComment;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.ACTIVE;

    @Builder
    private PostComment(Account author, PostHeader header, PostComment parentComment, String content) {
        this.author = author;
        this.header = header;
        this.parentComment = parentComment;
        this.content = content;
    }


}
