package com.filmdoms.community.board.notice.data.dto.response;

import com.filmdoms.community.board.data.dto.BoardHeadCoreDetailResponseDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeDetailResponseDto extends BoardHeadCoreDetailResponseDto {

    //메인 페이지 정보는 상세 조회시 필요 없음
    //comment 구현되면 comment 정보도 같이 넘기기
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public NoticeDetailResponseDto(NoticeHeader noticeHeader) {
        super(noticeHeader);
        this.startDate = noticeHeader.getStartDate();
        this.endDate = noticeHeader.getEndDate();
    }
}
