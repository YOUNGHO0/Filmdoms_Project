package com.filmdoms.community.article.data.dto.request.update;

import com.filmdoms.community.article.data.entity.extra.Critic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CriticUpdateRequestDto extends ParentUpdateRequestDto {


    public void updateEntity(Critic critic, String mainImage) {
        critic.update(mainImage);
    }
}
