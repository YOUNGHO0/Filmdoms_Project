package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class FilmUniverseListResponseResponseDto extends ParentBoardListResponseDto {

    public FilmUniverseListResponseResponseDto(Article article) {
        super(article);
    }

    public static FilmUniverseListResponseResponseDto from(Article article) {
        return new FilmUniverseListResponseResponseDto(article);
    }
}
