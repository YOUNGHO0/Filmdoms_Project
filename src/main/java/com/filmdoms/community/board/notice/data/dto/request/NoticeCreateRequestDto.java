package com.filmdoms.community.board.notice.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NoticeCreateRequestDto {

    private String title;
    private Long accountId;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
