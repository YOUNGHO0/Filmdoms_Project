package com.filmdoms.community.article.service;

import com.filmdoms.community.article.data.constant.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleServiceFactory {

    private final ArticleService articleService;
    private final FilmUniverseService filmUniverseService;
    private final CriticService criticService;

    public ArticleExecutor getArticleExecutor(Category category) {
        switch (category) {
            case FILM_UNIVERSE -> {
                return filmUniverseService;
            }
            case MOVIE -> {
                return articleService;
            }
            case CRITIC -> {
                return criticService;
            }
            default -> throw new IllegalArgumentException("Unsupported category: " + category);

        }

    }
}
