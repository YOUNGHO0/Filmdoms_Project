package com.filmdoms.community.article.data.dto.response;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class ArticleMainPageResponseDto extends MainPageResponseDto { //recent, movie 메인페이지
    private int commentNum;

    private ArticleMainPageResponseDto(Long id, Category category, Tag tag, String title, int commentNum) {
        super(id, category, tag, title);
        this.commentNum = commentNum;
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
