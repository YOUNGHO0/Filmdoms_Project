package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

/**
 * 메인 페이지 응답 DTO의 공통 필드를 모아 놓은 상속 전용 DTO
 */
@Getter
public abstract class ParentMainPageResponseDto {
    private Long id;
    private Category category;
    private Tag tag;
    private String title;

    protected ParentMainPageResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
    }
}
