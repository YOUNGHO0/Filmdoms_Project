package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.entity.extra.Critic;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 메인 페이지의 비평 카테고리, recent 게시글 응답 DTO
 */
@Getter
public class CriticMainPageResponseDto extends ParentMainPageResponseDto {
    private String mainImage;
    private SimpleAccountResponseDto author;
    private String description;
    private long createdAt;

    private CriticMainPageResponseDto(Critic critic) {
        super(critic.getArticle());
        this.mainImage = critic.getMainImage();
        this.author = SimpleAccountResponseDto.from(critic.getArticle().getAuthor());
        this.description = critic.getArticle().getContent().getContent();
        this.createdAt = ZonedDateTime.of(critic.getArticle().getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static CriticMainPageResponseDto from(Critic critic) {
        return new CriticMainPageResponseDto(critic);
    }
}
