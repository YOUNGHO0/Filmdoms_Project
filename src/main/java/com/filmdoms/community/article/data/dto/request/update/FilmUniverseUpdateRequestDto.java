package com.filmdoms.community.article.data.dto.request.update;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.entity.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FilmUniverseUpdateRequestDto extends ParentUpdateRequestDto {

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @NotNull
    private Long mainImageId;

    public FilmUniverseUpdateRequestDto(String title, Category category, Tag tag, String content, boolean containsImage, LocalDateTime startAt, LocalDateTime endAt, Long mainImageId) {
        super(title, category, tag, content, containsImage);
        this.startAt = startAt;
        this.endAt = endAt;
        this.mainImageId = mainImageId;
    }

    public void updateEntity(FilmUniverse filmUniverse, File mainImage) {
        filmUniverse.update(mainImage, startAt, endAt);
    }
}
