package com.filmdoms.community.article.data.dto.request.create;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CriticCreateRequestDto extends ParentCreateRequestDto {


    //테스트 용도
    @Builder
    public CriticCreateRequestDto(String title, Category category, Tag tag, String content) {
        super(title, category, tag, content);
    }

    public Critic toEntity(Article article, String mainImage) {
        Critic critic = Critic.builder()
                .article(article)
                .mainImage(mainImage)
                .build();

        return critic;
    }
}
