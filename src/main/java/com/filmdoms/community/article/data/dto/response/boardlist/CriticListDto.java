package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class CriticListDto extends ParentBoardListDto {
    public CriticListDto(Article article) {
        super(article);
    }

    public static CriticListDto from(Article article) {
        return new CriticListDto(article);
    }
}
