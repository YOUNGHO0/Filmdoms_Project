package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class FilmUniverseListDto extends ParentBoardListDto{

    public FilmUniverseListDto(Article article) {
        super(article);
    }

    public static FilmUniverseListDto  from(Article article)
    {
        return new FilmUniverseListDto(article);
    }
}
