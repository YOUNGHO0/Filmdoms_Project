package com.filmdoms.community.account.data.dto.response.profile;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class SimpleArticleResponseDto {

    private Long id;
    private Category category;

    private SimpleArticleResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
    }

    public static SimpleArticleResponseDto from(Article article) {
        return new SimpleArticleResponseDto(article);
    }
}
