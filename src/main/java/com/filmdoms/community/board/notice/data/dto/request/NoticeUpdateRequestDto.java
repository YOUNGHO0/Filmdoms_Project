package com.filmdoms.community.board.notice.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor //테스트에 필요
@NoArgsConstructor
@Getter
public class NoticeUpdateRequestDto {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long mainImageId;
    private Set<Long> contentImageId;
}
