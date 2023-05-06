package com.filmdoms.community.account.data.dto.response.profile;

import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.data.entity.Comment;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ProfileSingleCommentResponseDto {

    private Long id;
    private String content;
    private CommentStatus status;
    private int likes;
    private long createdAt;
    private long updatedAt;
    private SimpleArticleResponseDto article;

    private ProfileSingleCommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.status = comment.getStatus();
        this.likes = comment.getVoteCount();
        this.createdAt = ZonedDateTime.of(comment.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(comment.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.article = SimpleArticleResponseDto.from(comment.getArticle());
    }

    public static ProfileSingleCommentResponseDto from(Comment comment) {
        return new ProfileSingleCommentResponseDto(comment);
    }
}
