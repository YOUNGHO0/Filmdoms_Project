package com.filmdoms.community.article.data.dto.request.update;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.file.data.entity.File;
import jakarta.validation.constraints.NotNull;
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

    public void updateEntity(FilmUniverse filmUniverse, File mainImage) {
        filmUniverse.update(mainImage, startAt, endAt);
    }
}
