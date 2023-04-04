package com.filmdoms.community.article.data.dto;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
public class ArticleControllerToServiceDto {

    private String title;
    private Category category;
    private Tag tag;
    private Account author;
    private String content;
    private Set<Long> contentImageId;


    public ArticleControllerToServiceDto (String title, Category category, Tag tag, Account author, String content, Set<Long> contentImageId )
    {
        this.title = title;
        this.category = category;
        this.tag = tag;
        this.author = author;
        this.content = content;
        this.contentImageId =  contentImageId;
    }

    public static ArticleControllerToServiceDto from(ArticleRequestDto dto, Category category, Tag tag, Account author)
    {
        return ArticleControllerToServiceDto.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .category(category)
                .contentImageId(dto.getContentImageId())
                .tag(tag)
                .build();

    }
}
