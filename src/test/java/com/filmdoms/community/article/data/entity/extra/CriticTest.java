package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.testentityprovider.TestAccountProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Critic 엔티티 테스트")
class CriticTest {

    @Test
    @DisplayName("Article 카테고리가 잘못 설정되면 엔티티 생성자가 예외를 발생시킴")
    void invalidCategory() {

        //given
        Account author = TestAccountProvider.get();
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .author(author)
                .category(Category.MOVIE)
                .tag(Tag.CRITIC_ACTOR)
                .build();
        Critic.CriticBuilder criticBuilder = Critic.builder()
                .article(article);

        //when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> criticBuilder.build());
    }

    @Test
    @DisplayName("Article 태그가 잘못 설정되면 엔티티 생성자가 예외를 발생시킴")
    void invalidTag() {

        //given
        Account author = TestAccountProvider.get();
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .author(author)
                .category(Category.CRITIC)
                .tag(Tag.OTT)
                .build();
        Critic.CriticBuilder criticBuilder = Critic.builder()
                .article(article);

        //when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> criticBuilder.build());
    }

    @Test
    @DisplayName("Article 카테고리와 태그가 알맞게 설정되면 정상적으로 엔티티가 생성됨")
    void validCategoryAndTag() {

        //given
        Account author = TestAccountProvider.get();
        Article article = Article.builder()
                .title("test title")
                .content("test content")
                .author(author)
                .category(Category.CRITIC)
                .tag(Tag.CRITIC_ACTOR)
                .build();
        Critic.CriticBuilder criticBuilder = Critic.builder()
                .article(article);

        //when & then
        Critic critic = criticBuilder.build();
    }
}