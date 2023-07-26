package com.filmdoms.community.comment.data.dto.response;

import com.filmdoms.community.account.data.dto.response.DetailPageAccountResponseDto;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.data.entity.Comment;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 게시글 상세 페이지에서 댓글 정보를 주기 위한 응답 DTO, 자식 댓글 용도
 */
@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private CommentStatus status;
    private int likes;
    private long createdAt;
    private long updatedAt;
    private boolean isManagerComment;
    private DetailPageAccountResponseDto author;

    protected CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.createdAt = ZonedDateTime.of(comment.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(comment.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.isManagerComment = comment.isManagerComment();

        if (comment.getStatus() == CommentStatus.ACTIVE) {
            this.content = comment.getContent();
            this.status = comment.getStatus();
            this.likes = comment.getVoteCount();
            if (comment.getAuthor() != null)
                this.author = DetailPageAccountResponseDto.from(comment.getAuthor());
            else
                this.author = null;
        } else {
            this.content = "삭제된 댓글입니다.";
            this.status = comment.getStatus();
            this.likes = 0;
            this.author = null;
        }


    }

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(comment);
    }
}
