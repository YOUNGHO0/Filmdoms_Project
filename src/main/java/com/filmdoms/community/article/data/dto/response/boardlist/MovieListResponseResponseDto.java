package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class MovieListResponseResponseDto extends ParentBoardListResponseDto {
    public MovieListResponseResponseDto(Article article) {
        super(article);
    }

    public static MovieListResponseResponseDto from(Article article) {
        return new MovieListResponseResponseDto(article);
    }
}
