package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.entity.File;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 공지 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class FilmUniverseDetailResponseDto extends ArticleDetailResponseDto {

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private FilmUniverseDetailResponseDto(FilmUniverse filmUniverse, List<File> images, boolean isVoted) {
        super(filmUniverse.getArticle(), images, isVoted);
        this.startAt = filmUniverse.getStartDate();
        this.endAt = filmUniverse.getEndDate();
    }

    public static FilmUniverseDetailResponseDto from(FilmUniverse filmUniverse, List<File> images, boolean isVoted) {
        return new FilmUniverseDetailResponseDto(filmUniverse, images, isVoted);
    }
}
