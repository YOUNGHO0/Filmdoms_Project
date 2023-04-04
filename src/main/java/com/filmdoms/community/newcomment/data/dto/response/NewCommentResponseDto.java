package com.filmdoms.community.newcomment.data.dto.response;

import com.filmdoms.community.account.data.dto.response.DetailPageAccountResponseDto;
import com.filmdoms.community.board.data.constant.CommentStatus;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 상세 페이지에서 댓글 정보를 주기 위한 응답 DTO, 자식 댓글 용도
 */
@Getter
public class NewCommentResponseDto {

    private Long id;
    private String content;
    private CommentStatus status;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;
    private boolean isManagerComment;
    private DetailPageAccountResponseDto author;

    protected NewCommentResponseDto(NewComment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.status = comment.getStatus();
        this.dateCreated = comment.getDateCreated();
        this.dateLastModified = comment.getDateLastModified();
        this.isManagerComment = comment.isManagerComment();
        this.author = DetailPageAccountResponseDto.from(comment.getAuthor());
    }

    public static NewCommentResponseDto from(NewComment comment) {
        return new NewCommentResponseDto(comment);
    }
}
