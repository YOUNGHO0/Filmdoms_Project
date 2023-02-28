package com.filmdoms.community.board.notice.data.dto.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor //테스트에 필요
@NoArgsConstructor
@Getter
public class NoticeCreateRequestDto {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long mainImageId;
    private Set<Long> contentImageId;
}
