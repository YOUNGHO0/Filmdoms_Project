package com.filmdoms.community.article.data.dto.request.create;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.entity.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FilmUniverseCreateRequestDto extends ParentCreateRequestDto {

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @NotNull
    private Long mainImageId;

    @Builder
    public FilmUniverseCreateRequestDto(String title, Category category, Tag tag, String content, boolean containsImage, LocalDateTime startAt, LocalDateTime endAt, Long mainImageId) {
        super(title, category, tag, content, containsImage);
        this.startAt = startAt;
        this.endAt = endAt;
        this.mainImageId = mainImageId;
    }

    public FilmUniverse toEntity(Article article, File mainImage) {
        FilmUniverse filmUniverse = FilmUniverse.builder()
                .startDate(startAt)
                .endDate(endAt)
                .article(article)
                .mainImage(mainImage)
                .build();

        return filmUniverse;
    }
}
