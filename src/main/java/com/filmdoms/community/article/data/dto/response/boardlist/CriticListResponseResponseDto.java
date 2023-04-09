package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class CriticListResponseResponseDto extends ParentBoardListResponseDto {
    public CriticListResponseResponseDto(Article article) {
        super(article);
    }

    public static CriticListResponseResponseDto from(Article article) {
        return new CriticListResponseResponseDto(article);
    }
}
