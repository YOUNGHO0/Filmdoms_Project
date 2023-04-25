package com.filmdoms.community.article.data.dto.request.create;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ArticleCreateRequestDto extends ParentCreateRequestDto {

    //테스트 용도
    @Builder
    public ArticleCreateRequestDto(String title, Category category, Tag tag, String content, boolean containsImage) {
        super(title, category, tag, content, containsImage);
    }
}
