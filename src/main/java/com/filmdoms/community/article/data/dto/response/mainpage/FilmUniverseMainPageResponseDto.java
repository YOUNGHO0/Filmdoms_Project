package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 메인 페이지의 공지 카테고리 게시글 응답 DTO
 */
@Getter
public class FilmUniverseMainPageResponseDto extends ParentMainPageResponseDto {
    private FileResponseDto mainImage;
    private SimpleAccountResponseDto writer;
    private LocalDate startAt;
    private LocalDate endAt;

    private FilmUniverseMainPageResponseDto(FilmUniverse filmUniverse) {
        super(filmUniverse.getArticle());
        this.mainImage = FileResponseDto.from(filmUniverse.getMainImage());
        this.writer = SimpleAccountResponseDto.from(filmUniverse.getArticle().getAuthor());
        this.startAt = filmUniverse.getStartDate().toLocalDate();
        this.endAt = filmUniverse.getEndDate().toLocalDate();
    }

    public static FilmUniverseMainPageResponseDto from(FilmUniverse filmUniverse) {
        return new FilmUniverseMainPageResponseDto(filmUniverse);
    }
}
