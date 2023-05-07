package com.filmdoms.community.account.data.dto.response.profile;

import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProfileArticleResponseDto {

    private List<ProfileSingleArticleResponseDto> articles;
    private SimplePageInfoResponseDto pageInfo;

    private ProfileArticleResponseDto(Page<Article> articlePage) {
        this.articles = articlePage.getContent()
                .stream()
                .map(ProfileSingleArticleResponseDto::from)
                .toList();
        this.pageInfo = SimplePageInfoResponseDto.from(articlePage);
    }

    public static ProfileArticleResponseDto from(Page<Article> articlePage) {
        return new ProfileArticleResponseDto(articlePage);
    }
}
