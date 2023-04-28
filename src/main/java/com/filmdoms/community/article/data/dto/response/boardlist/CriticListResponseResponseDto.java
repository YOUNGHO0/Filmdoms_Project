package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.file.data.entity.File;
import lombok.Getter;

@Getter
public class CriticListResponseResponseDto extends ParentBoardListResponseDto {

    String mainImage;

    public CriticListResponseResponseDto(Article article, File mainImage) {
        super(article);
        this.mainImage = mainImage.getUuidFileName();

    }

    public static CriticListResponseResponseDto from(Critic critic) {
        return new CriticListResponseResponseDto(critic.getArticle(), critic.getMainImage());
    }
}
