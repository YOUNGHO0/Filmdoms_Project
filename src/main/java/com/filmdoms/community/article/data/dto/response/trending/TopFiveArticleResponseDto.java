package com.filmdoms.community.article.data.dto.response.trending;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

@Getter
public class TopFiveArticleResponseDto {

    Long id;
    String title;
    SimpleAccountResponseDto author;

    public TopFiveArticleResponseDto(Long id, String title, SimpleAccountResponseDto author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public static TopFiveArticleResponseDto from(Article article) {
        return new TopFiveArticleResponseDto(article.getId(), article.getTitle(), SimpleAccountResponseDto.from(article.getAuthor()));
    }
}
