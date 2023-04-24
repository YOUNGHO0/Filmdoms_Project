package com.filmdoms.community.article.data.dto.request.create;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.file.data.entity.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CriticCreateRequestDto extends ParentCreateRequestDto {

    private Long mainImageId;

    //테스트 용도
    @Builder
    public CriticCreateRequestDto(String title, Category category, Tag tag, String content, boolean containsImage, Long mainImageId) {
        super(title, category, tag, content, containsImage);
        this.mainImageId = mainImageId;
    }

    public Critic toEntity(Article article, File mainImage) {
        Critic critic = Critic.builder()
                .article(article)
                .mainImage(mainImage)
                .build();

        return critic;
    }
}
