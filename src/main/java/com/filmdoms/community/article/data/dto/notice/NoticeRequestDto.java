package com.filmdoms.community.article.data.dto.notice;

import com.filmdoms.community.article.data.dto.ParentRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class NoticeRequestDto extends ParentRequestDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
