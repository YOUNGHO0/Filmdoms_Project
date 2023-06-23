package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ArticleCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.testconfig.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.testentityprovider.TestAccountProvider;
import com.filmdoms.community.testentityprovider.TestFileProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTestWithJpaAuditing
@Import({ArticleService.class})
@ActiveProfiles("test")
@DisplayName("게시글 서비스 통합 테스트")
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FilmUniverseRepository filmUniverseRepository;

    @Autowired
    CriticRepository criticRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("영화 게시판 - 정상 요청에 대해 게시글 생성이 이루어진다.")
    void createArticleCategoryMovie() {
        //given
        Account testAuthor = TestAccountProvider.get();
        accountRepository.save(testAuthor);

        ArticleCreateRequestDto requestDto = ArticleCreateRequestDto.builder()
                .title("test title")
                .category(Category.MOVIE)
                .tag(Tag.MOVIE)
                .content("test content")
                .containsImage(false)
                .build();

        //when
        ArticleCreateResponseDto responseDto = articleService.createArticle(requestDto, AccountDto.from(testAuthor));
        em.flush();
        em.clear();

        //then
        Article article = articleRepository.findById(responseDto.getId()).get();
        assertThat(article.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(article.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(article.getTag()).isEqualTo(requestDto.getTag());
        assertThat(article.getContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(article.isContainsImage()).isEqualTo(requestDto.isContainsImage());
        assertThat(article.getAuthor().getId()).isEqualTo(testAuthor.getId());
    }

    @Test
    @DisplayName("필름 유니버스 게시판 - 정상 요청에 대해 게시글 생성이 이루어진다.")
    void createArticleCategoryFilmUniverse() {
        //given
        Account testAuthor = TestAccountProvider.get();
        accountRepository.save(testAuthor);
        File testMainImage = TestFileProvider.get();
        fileRepository.save(testMainImage);

        LocalDateTime startDate = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        LocalDateTime endDate = startDate.plusDays(7);
        FilmUniverseCreateRequestDto requestDto = FilmUniverseCreateRequestDto.builder()
                .title("test title")
                .category(Category.FILM_UNIVERSE)
                .tag(Tag.CLUB)
                .content("test content")
                .containsImage(true)
                .startAt(startDate)
                .endAt(endDate)
                .mainImageId(testMainImage.getId())
                .build();

        //when
        ArticleCreateResponseDto responseDto = articleService.createArticle(requestDto, AccountDto.from(testAuthor));
        em.flush();
        em.clear();

        //then
        Article article = articleRepository.findById(responseDto.getId()).get();
        FilmUniverse filmUniverse = filmUniverseRepository.findByArticleId(responseDto.getId()).get();
        assertThat(article.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(article.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(article.getTag()).isEqualTo(requestDto.getTag());
        assertThat(article.getContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(article.isContainsImage()).isEqualTo(requestDto.isContainsImage());
        assertThat(article.getAuthor().getId()).isEqualTo(testAuthor.getId());
        assertThat(filmUniverse.getStartDate()).isEqualTo(requestDto.getStartAt());
        assertThat(filmUniverse.getEndDate()).isEqualTo(requestDto.getEndAt());
        assertThat(filmUniverse.getMainImage().getId()).isEqualTo(requestDto.getMainImageId());
    }

    @Test
    @DisplayName("비평 게시판 - 정상 요청에 대해 게시글 생성이 이루어진다.")
    void createArticleCategoryCritic() {
        //given
        Account testAuthor = TestAccountProvider.get();
        accountRepository.save(testAuthor);
        File testMainImage = TestFileProvider.get();
        fileRepository.save(testMainImage);

        CriticCreateRequestDto requestDto = CriticCreateRequestDto.builder()
                .title("test title")
                .category(Category.CRITIC)
                .tag(Tag.CRITIC_ACTOR)
                .content("test content")
                .containsImage(true)
                .mainImageId(testMainImage.getId())
                .build();

        //when
        ArticleCreateResponseDto responseDto = articleService.createArticle(requestDto, AccountDto.from(testAuthor));
        em.flush();
        em.clear();

        //then
        Article article = articleRepository.findById(responseDto.getId()).get();
        Critic critic = criticRepository.findByArticleId(responseDto.getId()).get();
        assertThat(article.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(article.getCategory()).isEqualTo(requestDto.getCategory());
        assertThat(article.getTag()).isEqualTo(requestDto.getTag());
        assertThat(article.getContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(article.isContainsImage()).isEqualTo(requestDto.isContainsImage());
        assertThat(article.getAuthor().getId()).isEqualTo(testAuthor.getId());
        assertThat(critic.getMainImage().getId()).isEqualTo(requestDto.getMainImageId());
    }
}