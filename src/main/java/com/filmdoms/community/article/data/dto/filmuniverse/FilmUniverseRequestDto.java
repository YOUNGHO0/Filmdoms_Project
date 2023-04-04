package com.filmdoms.community.article.data.dto.filmuniverse;

import com.filmdoms.community.article.data.dto.ParentRequestDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class FilmUniverseRequestDto extends ParentRequestDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
