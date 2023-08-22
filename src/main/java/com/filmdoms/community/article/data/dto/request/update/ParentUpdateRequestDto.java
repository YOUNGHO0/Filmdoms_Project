package com.filmdoms.community.article.data.dto.request.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "category",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "MOVIE", value = ArticleUpdateRequestDto.class),
        @JsonSubTypes.Type(name = "FILM_UNIVERSE", value = FilmUniverseUpdateRequestDto.class),
        @JsonSubTypes.Type(name = "CRITIC", value = CriticUpdateRequestDto.class)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED) //테스트 용도
@Getter
public abstract class ParentUpdateRequestDto {

    @NotBlank
    @Size(max = 100)
    private String title;
    private Category category;
    private Tag tag;
    @NotBlank
    @Size(max = 50000)
    private String content;


    public void updateEntity(Article article, int imageCount) {
        boolean containsImage = false;

        if (this instanceof FilmUniverseUpdateRequestDto || this instanceof CriticUpdateRequestDto || imageCount > 0) {
            containsImage = true;
        }
        article.update(title, tag, content, containsImage);
    }
}
