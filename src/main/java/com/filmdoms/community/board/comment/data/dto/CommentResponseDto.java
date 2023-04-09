package com.filmdoms.community.board.comment.data.dto;

import com.filmdoms.community.account.data.dto.response.CommentAuthorResponseDto;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.board.data.constant.CommentStatus;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto { //자식 댓글용 응답 DTO

    private Long id;
    private CommentAuthorResponseDto author;
    private String content;
    private CommentStatus status;
    private long createdAt;
    private long updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = new CommentAuthorResponseDto(comment.getAuthor());
        this.content = comment.getContent();
        this.status = comment.getStatus();
        this.createdAt = ZonedDateTime.of(comment.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(comment.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
