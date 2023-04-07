package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class MovieListDto extends ParentBoardListDto {
    public MovieListDto(Article article) {
        super(article);
    }

    public static MovieListDto from(Article article) {
        return new MovieListDto(article);
    }
}
