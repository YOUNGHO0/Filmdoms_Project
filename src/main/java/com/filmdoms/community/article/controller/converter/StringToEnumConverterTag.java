package com.filmdoms.community.article.controller.converter;

import com.filmdoms.community.article.data.constant.Tag;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverterTag implements Converter<String, Tag> {

    @Override
    public Tag convert(String source) {
        try {
            return Tag.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
