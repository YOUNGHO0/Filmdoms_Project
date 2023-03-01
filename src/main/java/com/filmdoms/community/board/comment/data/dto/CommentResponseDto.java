package com.filmdoms.community.board.comment.data.dto;

import com.filmdoms.community.account.data.dto.response.CommentAuthorResponseDto;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.board.data.constant.CommentStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private CommentAuthorResponseDto author;
    private String content;
    private CommentStatus status;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = new CommentAuthorResponseDto(comment.getAuthor());
        this.content = comment.getContent();
        this.status = comment.getStatus();
        this.dateCreated = comment.getDateCreated();
        this.dateLastModified = comment.getDateLastModified();
    }
}
