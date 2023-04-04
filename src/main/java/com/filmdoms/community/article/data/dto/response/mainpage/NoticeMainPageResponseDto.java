package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.entity.extra.Notice;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 메인 페이지의 공지 카테고리 게시글 응답 DTO
 */
@Getter
public class NoticeMainPageResponseDto extends ParentMainPageResponseDto {
    private FileResponseDto mainImage;
    private SimpleAccountResponseDto author;
    private LocalDate startDate;
    private LocalDate endDate;

    private NoticeMainPageResponseDto(Notice notice) {
        super(notice.getArticle());
        this.mainImage = FileResponseDto.from(notice.getMainImage());
        this.author = SimpleAccountResponseDto.from(notice.getArticle().getAuthor());
        this.startDate = notice.getStartDate().toLocalDate();
        this.endDate = notice.getEndDate().toLocalDate();
    }

    public static NoticeMainPageResponseDto from(Notice notice) {
        return new NoticeMainPageResponseDto(notice);
    }
}
