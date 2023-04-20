package com.filmdoms.community.article.controller.converter;

import com.filmdoms.community.article.data.constant.SearchMethod;
import com.filmdoms.community.article.data.constant.Tag;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterSearchMethod implements Converter<String, SearchMethod> {
    @Override
    public SearchMethod convert(String source) {
        try {
            return SearchMethod.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
