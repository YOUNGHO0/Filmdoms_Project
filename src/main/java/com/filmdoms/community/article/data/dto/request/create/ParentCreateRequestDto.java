package com.filmdoms.community.article.data.dto.request.create;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.filmdoms.community.exception.ValidationMessage.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "category",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "MOVIE", value = ArticleCreateRequestDto.class),
        @JsonSubTypes.Type(name = "FILM_UNIVERSE", value = FilmUniverseCreateRequestDto.class),
        @JsonSubTypes.Type(name = "CRITIC", value = CriticCreateRequestDto.class)
})
@NoArgsConstructor
@AllArgsConstructor //테스트 용도
@Getter
public abstract class ParentCreateRequestDto {

    @NotBlank(message = TITLE_NOT_BLANK)
    @Size(max = 100, message = TITLE_SIZE)
    private String title;
    private Category category;
    private Tag tag;
    @NotBlank(message = CONTENT_NOT_BLANK)
    @Size(max = 50000, message = CONTENT_SIZE)
    private String content;
    boolean containsImage;

    public Article toEntity(Account author) {
        //필름 유니버스, 비평 게시판은 항상 이미지를 포함
        if (this instanceof FilmUniverseCreateRequestDto || this instanceof CriticCreateRequestDto) {
            containsImage = true;
        }

        Article article = Article.builder()
                .title(title)
                .author(author)
                .category(category)
                .tag(tag)
                .content(content)
                .containsImage(containsImage)
                .build();

        return article;
    }
}
