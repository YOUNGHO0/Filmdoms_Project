package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CriticRepository criticRepository;
    private final AccountRepository accountRepository;
    private final FilmUniverseRepository filmUniverseRepository;

    public Response deleteAllArticlesService(Category category, AccountDto accountDto) {

        switch (category) {
            case MOVIE ->
                    articleRepository.findAll().stream().filter(article -> article.getCategory() == Category.MOVIE).forEach(movie -> articleService.deleteArticle(Category.MOVIE, movie.getId(), accountDto));
            case CRITIC ->
                    criticRepository.findAll().stream().forEach(critic -> articleService.deleteArticle(Category.CRITIC, critic.getArticle().getId(), accountDto));
            case FILM_UNIVERSE ->
                    filmUniverseRepository.findAll().stream().forEach(critic -> articleService.deleteArticle(Category.FILM_UNIVERSE, critic.getArticle().getId(), accountDto));
        }

        return Response.success();
    }
}
