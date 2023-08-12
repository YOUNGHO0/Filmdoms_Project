package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 메인 페이지의 공지 카테고리 게시글 응답 DTO
 */
@Getter
public class FilmUniverseMainPageResponseDto extends ParentMainPageResponseDto {
    private String mainImage;
    private SimpleAccountResponseDto author;
    private LocalDate startAt;
    private LocalDate endAt;

    private FilmUniverseMainPageResponseDto(FilmUniverse filmUniverse) {
        super(filmUniverse.getArticle());
        this.mainImage = filmUniverse.getMainImage();
        this.author = SimpleAccountResponseDto.from(filmUniverse.getArticle().getAuthor());
        this.startAt = filmUniverse.getStartDate().toLocalDate();
        this.endAt = filmUniverse.getEndDate().toLocalDate();
    }

    public static FilmUniverseMainPageResponseDto from(FilmUniverse filmUniverse) {
        return new FilmUniverseMainPageResponseDto(filmUniverse);
    }
}
