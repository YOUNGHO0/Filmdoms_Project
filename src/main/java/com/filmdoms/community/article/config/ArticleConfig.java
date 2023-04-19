package com.filmdoms.community.article.config;

import com.filmdoms.community.article.controller.converter.StringToEnumConverterCategory;
import com.filmdoms.community.article.controller.converter.StringToEnumConverterSearchMethod;
import com.filmdoms.community.article.controller.converter.StringToEnumConverterTag;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ArticleConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToEnumConverterCategory());
        registry.addConverter(new StringToEnumConverterTag());
        registry.addConverter(new StringToEnumConverterSearchMethod());
    }
}
