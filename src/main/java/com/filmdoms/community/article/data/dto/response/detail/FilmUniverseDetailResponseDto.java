package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공지 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class FilmUniverseDetailResponseDto extends ArticleDetailResponseDto {

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private FilmUniverseDetailResponseDto(FilmUniverse filmUniverse, boolean isVoted) {
        super(filmUniverse.getArticle(), isVoted);
        this.startAt = filmUniverse.getStartDate();
        this.endAt = filmUniverse.getEndDate();
    }

    public static FilmUniverseDetailResponseDto from(FilmUniverse filmUniverse, boolean isVoted) {
        return new FilmUniverseDetailResponseDto(filmUniverse, isVoted);
    }
}
