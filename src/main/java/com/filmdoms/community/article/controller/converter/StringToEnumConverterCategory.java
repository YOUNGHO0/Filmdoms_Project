package com.filmdoms.community.article.controller.converter;

import com.filmdoms.community.article.data.constant.Category;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterCategory implements Converter<String, Category>{

    @Override
    public Category convert(String category) {
        try {
            return Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

    }
}