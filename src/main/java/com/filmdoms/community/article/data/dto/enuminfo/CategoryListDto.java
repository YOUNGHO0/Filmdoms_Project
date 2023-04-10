package com.filmdoms.community.article.data.dto.enuminfo;

import com.filmdoms.community.article.data.constant.Category;
import lombok.Getter;

@Getter
public class CategoryListDto {

    Category category;
    String description;

    public CategoryListDto(Category category) {

        this.category = category;
        this.description = category.getDescription();
    }

    public static CategoryListDto from(Category category) {
        return new CategoryListDto(category);
    }
}
