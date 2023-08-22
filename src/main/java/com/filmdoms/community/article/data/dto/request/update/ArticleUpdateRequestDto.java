package com.filmdoms.community.article.data.dto.request.update;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleUpdateRequestDto extends ParentUpdateRequestDto {

    public ArticleUpdateRequestDto(String title, Category category, Tag tag, String content) {
        super(title, category, tag, content);
    }
}
