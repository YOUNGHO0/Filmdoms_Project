package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.article.data.entity.extra.Notice;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 공지 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class NoticeDetailResponseDto extends ArticleDetailResponseDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private NoticeDetailResponseDto(Notice notice, List<File> images, List<NewComment> comments) {
        super(notice.getArticle(), images, comments);
        this.startDate = notice.getStartDate();
        this.endDate = notice.getEndDate();
    }

    public static NoticeDetailResponseDto from(Notice notice, List<File> images, List<NewComment> comments) {
        return new NoticeDetailResponseDto(notice, images, comments);
    }
}
