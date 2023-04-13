package com.filmdoms.community.article.data.dto.response.trending;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class TopFiveArticleResponseDto {

    private Long id;
    private String title;
    private SimpleAccountResponseDto author;
    private boolean containsImage;

    private Category category;


    public TopFiveArticleResponseDto(Long id, String title, SimpleAccountResponseDto author, boolean containsImage, Category category) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.containsImage = containsImage;
        this.author = author;
    }

    public static TopFiveArticleResponseDto from(Article article) {
        return new TopFiveArticleResponseDto(article.getId(), article.getTitle(), SimpleAccountResponseDto.from(article.getAuthor()), article.isContainsImage(), article.getCategory());
    }
}
