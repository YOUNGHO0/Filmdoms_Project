package com.filmdoms.community.article.data.dto.response;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class MainPageResponseDto {
    private Long id;
    private Category category;
    private Tag tag;
    private String title;
}
