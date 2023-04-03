package com.filmdoms.community.article.data.dto.response;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class ArticleMainPageResponseDto extends MainPageResponseDto { //recent, movie 메인페이지
    private int commentCount;

    private ArticleMainPageResponseDto(Long id, Category category, Tag tag, String title, int commentCount) {
        super(id, category, tag, title);
        this.commentCount = commentCount;
    }

    public static ArticleMainPageResponseDto from(Article article) {
        return new ArticleMainPageResponseDto(
                article.getId(),
                article.getCategory(),
                article.getTag(),
                article.getTitle(),
                article.getComments().size()
        );
    }
}
