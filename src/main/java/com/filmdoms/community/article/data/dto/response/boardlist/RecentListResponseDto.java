package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;

public class RecentListResponseDto extends ParentBoardListResponseDto {

    public RecentListResponseDto(Article article) {
        super(article);
    }

    public static RecentListResponseDto from(Article article) {
        return new RecentListResponseDto(article);
    }
}
