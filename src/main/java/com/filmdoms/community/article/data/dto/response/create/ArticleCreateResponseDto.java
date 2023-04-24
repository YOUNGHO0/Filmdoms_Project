package com.filmdoms.community.article.data.dto.response.create;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class ArticleCreateResponseDto {

    private Long id;
    private Category category;

    private ArticleCreateResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
    }

    public static ArticleCreateResponseDto from(Article article) {
        return new ArticleCreateResponseDto(article);
    }
}
