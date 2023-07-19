package com.filmdoms.community.article.data.dto.request.update;

import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.file.data.entity.File;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CriticUpdateRequestDto extends ParentUpdateRequestDto {

    @NotNull
    private Long mainImageId;

    public void updateEntity(Critic critic, File mainImage) {
        critic.update(mainImage);
    }
}
