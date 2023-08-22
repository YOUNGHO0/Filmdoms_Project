package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ArticleCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ArticleUpdateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.FilmUniverseUpdateRequestDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.comment.service.CommentService;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.testconfig.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.testentityprovider.TestAccountProvider;
import com.filmdoms.community.testentityprovider.TestCommentProvider;
import com.filmdoms.community.testentityprovider.TestFileProvider;
import com.filmdoms.community.vote.service.VoteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTestWithJpaAuditing
@Import({ArticleService.class, CommentService.class, VoteService.class})
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

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    @Autowired
    VoteService voteService;

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
                .startAt(startDate)
                .endAt(endDate)
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
        assertThat(article.getAuthor().getId()).isEqualTo(testAuthor.getId());
        assertThat(filmUniverse.getStartDate()).isEqualTo(requestDto.getStartAt());
        assertThat(filmUniverse.getEndDate()).isEqualTo(requestDto.getEndAt());

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
        assertThat(article.getAuthor().getId()).isEqualTo(testAuthor.getId());
    }

    @Test
    @DisplayName("영화 게시판 - 정상 요청에 대해 게시글 삭제가 이루어진다.")
    void deleteArticleCategoryMovie() {
        //given
        Account testAuthor = TestAccountProvider.get();
        Account voter = TestAccountProvider.get();
        accountRepository.saveAll(List.of(testAuthor, voter));

        Article article = Article.builder()
                .title("test title")
                .author(testAuthor)
                .category(Category.MOVIE)
                .tag(Tag.MOVIE)
                .content("test content")
                .containsImage(false)
                .build();
        articleRepository.save(article);

        Comment parentComment = TestCommentProvider.get(testAuthor, article, null);
        Comment childComment1 = TestCommentProvider.get(testAuthor, article, parentComment);
        Comment childComment2 = TestCommentProvider.get(testAuthor, article, parentComment);
        commentRepository.saveAll(List.of(parentComment, childComment1, childComment2));

        AccountDto voterAccountDto = AccountDto.from(voter);
        voteService.toggleVote(voterAccountDto, article.getId());
        commentService.toggleCommentVote(parentComment.getId(), voterAccountDto);
        commentService.toggleCommentVote(childComment1.getId(), voterAccountDto);
        commentService.toggleCommentVote(childComment2.getId(), voterAccountDto);
        em.flush();
        em.clear();

        //when
        articleService.deleteArticle(Category.MOVIE, article.getId(), AccountDto.from(testAuthor));
        em.flush();
        em.clear();

        //then
        assertThat(articleRepository.findById(article.getId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("필름 유니버스 게시판 - 정상 요청에 대해 게시글 삭제가 이루어진다.")
    void deleteArticleCategoryFilmUniverse() {
        //given
        Account testAuthor = TestAccountProvider.get();
        Account voter = TestAccountProvider.get();
        accountRepository.saveAll(List.of(testAuthor, voter));

        Article article = Article.builder()
                .title("test title")
                .author(testAuthor)
                .category(Category.FILM_UNIVERSE)
                .tag(Tag.CLUB)
                .content("test content")
                .containsImage(false)
                .build();

        File mainImageFile = TestFileProvider.get();
        fileRepository.save(mainImageFile);

        FilmUniverse filmUniverse = FilmUniverse.builder()
                .article(article)
                .startDate(LocalDateTime.of(2023, 7, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 8, 1, 0, 0))
                .build();

        filmUniverseRepository.save(filmUniverse);

        Comment parentComment = TestCommentProvider.get(testAuthor, article, null);
        Comment childComment1 = TestCommentProvider.get(testAuthor, article, parentComment);
        Comment childComment2 = TestCommentProvider.get(testAuthor, article, parentComment);
        commentRepository.saveAll(List.of(parentComment, childComment1, childComment2));

        AccountDto voterAccountDto = AccountDto.from(voter);
        voteService.toggleVote(voterAccountDto, article.getId());
        commentService.toggleCommentVote(parentComment.getId(), voterAccountDto);
        commentService.toggleCommentVote(childComment1.getId(), voterAccountDto);
        commentService.toggleCommentVote(childComment2.getId(), voterAccountDto);
        em.flush();
        em.clear();

        //when
        articleService.deleteArticle(Category.FILM_UNIVERSE, article.getId(), AccountDto.from(testAuthor));
        em.flush();
        em.clear();

        //then
        assertThat(articleRepository.findById(article.getId()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("영화 게시판 - 정상 요청에 대해 게시글 수정이 이루어진다.")
    void updateArticleCategoryMovie() {
        //given
        Account testAuthor = TestAccountProvider.get();
        accountRepository.save(testAuthor);

        Article article = Article.builder()
                .title("previous title")
                .author(testAuthor)
                .category(Category.MOVIE)
                .tag(Tag.MOVIE)
                .content("previous content")
                .containsImage(false)
                .build();
        articleRepository.save(article);

        ArticleUpdateRequestDto requestDto = new ArticleUpdateRequestDto("updated title", Category.MOVIE, Tag.OTT, "updated content");

        //when
        articleService.updateArticle(Category.MOVIE, article.getId(), AccountDto.from(testAuthor), requestDto);

        //then
        assertThat(article.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(article.getTag()).isEqualTo(requestDto.getTag());
        assertThat(article.getContent().getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("필름 유니버스 게시판 - 정상 요청에 대해 게시글 수정이 이루어진다.")
    void updateArticleCategoryFilmUniverse() {
        //given
        Account testAuthor = TestAccountProvider.get();
        accountRepository.save(testAuthor);

        Article article = Article.builder()
                .title("previous title")
                .author(testAuthor)
                .category(Category.FILM_UNIVERSE)
                .tag(Tag.CLUB)
                .content("previous content")
                .containsImage(true)
                .build();

        File previousMainImageFile = TestFileProvider.get();
        File updateMainImageFile = TestFileProvider.get();
        fileRepository.saveAll(List.of(previousMainImageFile, updateMainImageFile));

        LocalDateTime previousStartDate = LocalDateTime.of(2023, 7, 1, 0, 0);
        LocalDateTime previousEndDate = LocalDateTime.of(2023, 8, 1, 0, 0);

        FilmUniverse filmUniverse = FilmUniverse.builder()
                .article(article)
                .startDate(previousStartDate)
                .endDate(previousEndDate)
                .build();

        filmUniverseRepository.save(filmUniverse);

        LocalDateTime updateStartDate = LocalDateTime.of(2023, 9, 1, 0, 0);
        LocalDateTime updateEndDate = LocalDateTime.of(2023, 10, 1, 0, 0);

        FilmUniverseUpdateRequestDto requestDto = new FilmUniverseUpdateRequestDto("updated title", Category.FILM_UNIVERSE, Tag.COMPETITION, "updated content", updateStartDate, updateEndDate);

        //when
        articleService.updateArticle(Category.FILM_UNIVERSE, article.getId(), AccountDto.from(testAuthor), requestDto);

        //then
        assertThat(article.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(article.getTag()).isEqualTo(requestDto.getTag());
        assertThat(article.getContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(filmUniverse.getStartDate()).isEqualTo(requestDto.getStartAt());
        assertThat(filmUniverse.getEndDate()).isEqualTo(requestDto.getEndAt());
    }
}