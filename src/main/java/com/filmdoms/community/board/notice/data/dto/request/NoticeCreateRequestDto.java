package com.filmdoms.community.board.notice.data.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class NoticeCreateRequestDto {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long mainImageId;
    private List<Long> subImageIds;
}
