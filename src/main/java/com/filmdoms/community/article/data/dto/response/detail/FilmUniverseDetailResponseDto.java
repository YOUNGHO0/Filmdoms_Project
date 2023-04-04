package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 공지 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class FilmUniverseDetailResponseDto extends ArticleDetailResponseDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private FilmUniverseDetailResponseDto(FilmUniverse filmUniverse, List<File> images, List<NewComment> comments) {
        super(filmUniverse.getArticle(), images, comments);
        this.startDate = filmUniverse.getStartDate();
        this.endDate = filmUniverse.getEndDate();
    }

    public static FilmUniverseDetailResponseDto from(FilmUniverse filmUniverse, List<File> images, List<NewComment> comments) {
        return new FilmUniverseDetailResponseDto(filmUniverse, images, comments);
    }
}
