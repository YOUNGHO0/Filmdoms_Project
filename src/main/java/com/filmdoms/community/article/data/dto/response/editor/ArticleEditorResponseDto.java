package com.filmdoms.community.article.data.dto.response.editor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleEditorResponseDto {

    String editorContent;

    public static ArticleEditorResponseDto from(String content) {

        return new ArticleEditorResponseDto(content);
    }
}
