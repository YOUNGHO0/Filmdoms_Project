package com.filmdoms.community.board.notice.data.dto.response;

import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.imagefile.data.dto.ImageFileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
public class NoticeMainPageDto {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    private String title;
    private String mainImageUrl;
    private String startDate;
    private String endDate;

    public NoticeMainPageDto(NoticeHeader noticeHeader, String domain) {
        this.title = noticeHeader.getTitle();

        //지연로딩 일어나는 부분 -> 나중에 최적화
        this.mainImageUrl = ImageFileDto.from(noticeHeader.getImageFiles().get(0), domain).getFileUrl();

        //메인 페이지에서는 날짜 정보만 출력하므로 시간 정보는 생략
        this.startDate = noticeHeader.getStartDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        this.endDate = noticeHeader.getEndDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
    }
}
