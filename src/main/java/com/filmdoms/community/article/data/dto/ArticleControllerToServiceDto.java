package com.filmdoms.community.article.data.dto;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ArticleControllerToServiceDto {

    private String title;
    private Category category;

    private Tag tag;

    private Account author;
    private String content;



    public ArticleControllerToServiceDto (String title, Category category, Tag tag, Account author, String content)
    {
        this.title = title;
        this.category = category;
        this.tag = tag;
        this.author = author;
        this.content = content;
    }

    public static ArticleControllerToServiceDto from(ArticleRequestDto dto, Category category, Tag tag, Account author)
    {
        return ArticleControllerToServiceDto.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .category(category)
                .tag(tag)
                .build();

    }
}
