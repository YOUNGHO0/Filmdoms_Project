package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.DefaultProfileImage;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.DeleteAccountRequestDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import com.filmdoms.community.account.data.entity.Movie;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.FavoriteMovieRepository;
import com.filmdoms.community.account.repository.MovieRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ArticleCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.service.ArticleService;
import com.filmdoms.community.comment.data.dto.request.CommentCreateRequestDto;
import com.filmdoms.community.comment.data.dto.response.CommentCreateResponseDto;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.comment.service.CommentService;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.vote.data.entity.Vote;
import com.filmdoms.community.vote.repository.VoteRepository;
import com.filmdoms.community.vote.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@DisplayName("계정삭제 테스트")
class TotalAccountServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DefaultProfileImage defaultProfileImage;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;

    private List<Movie> findOrCreateFavoriteMovies(List<String> movieNames) {

        log.info("기존 영화 엔티티 호출");
        List<Movie> existingMovies = movieRepository.findAllByNames(movieNames);

        log.info("영화 엔티티 이름 추출");
        Set<String> existingMovieNames = existingMovies.stream()
                .map(Movie::getName)
                .collect(Collectors.toSet());

        log.info("새 영화 엔티티 생성");
        List<Movie> newMovies = movieNames.stream()
                .filter(name -> !existingMovieNames.contains(name))
                .map(Movie::new)
                .toList();

        log.info("새 영화 엔티티 저장");
        // JPA는 Id 생성을 AutoIncrement로 지정하면 벌크 insert를 수행할 수 없다. 어쩔 수 없이 쿼리 N개 발생.
        List<Movie> savedMovies = movieRepository.saveAll(newMovies);

        return Stream.concat(existingMovies.stream(), savedMovies.stream()).toList();
    }

    private Account getAccount() {
        // 계정 생성
        List<String> favMovie = List.of("토르", "스파이더맨", "영화123");
        JoinRequestDto requestDto = new JoinRequestDto("test@naver.com", "pass", "nickname1223", favMovie, "testetmailuuid");

        log.info("Account 엔티티 생성");
        Account newAccount = Account.builder()
                //.username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .role(AccountRole.USER)
                .profileImage(defaultProfileImage.getDefaultProfileImage())
                .build();

        log.info("Account 엔티티 저장");
        accountRepository.save(newAccount);

        log.info("영화 엔티티 호출 / 생성");
        List<Movie> movies = findOrCreateFavoriteMovies(requestDto.getFavoriteMovies());

        log.info("관심 영화 계정과 연결");
        List<FavoriteMovie> favoriteMovies = movies.stream()
                .map(movie -> FavoriteMovie.builder()
                        .movie(movie)
                        .account(newAccount)
                        .build())
                .toList();
        log.info("영화 {}", favoriteMovies.size());
        favoriteMovieRepository.saveAll(favoriteMovies);
        return newAccount;
    }

    private void createArticlesAndTryVoting(Account account) {
        createAllArticle(account);
        tryVote(account);
    }

    private void tryVote(Account account) {

        List<Category> categorys = Arrays.stream(Category.values()).toList();
        Pageable pageable = PageRequest.of(0, 20);

        for (Category category : categorys) {

            // 세부 게시판 좋아요 테스트
            List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                    .filter(tag -> tag.getCategory() == category)
                    .toList();
            for (Tag movieTag : movieTagList) {
                // 태그 있는 경우의 응답
                Page<? extends ParentBoardListResponseDto> articleList = articleService.getBoardList(category, movieTag, pageable);
                for (ParentBoardListResponseDto articles : articleList) {
                    voteService.toggleVote(AccountDto.from(account), articles.getId());
                }

            }
        }


    }

    private void createAllArticle(Account account) {

        List<Category> categorys = Arrays.stream(Category.values()).toList();
        List<Tag> movieTagList;
        for (Category category : categorys) {
            switch (category) {
                case MOVIE:
                    movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                            .filter(tag -> tag.getCategory() == category)
                            .toList();
                    for (Tag movieTag : movieTagList) {
                        createMovie("영화 게시글" + movieTag, category, movieTag, "내용입니다", false, account);
                    }
                    break;
                case FILM_UNIVERSE:
                    movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                            .filter(tag -> tag.getCategory() == category)
                            .toList();
                    for (Tag movieTag : movieTagList) {
                        File defaultImage = File.builder() //게시글과 매핑될 디폴트 이미지 생성
                                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                                .originalFileName("original_file_name")
                                .build();
                        File savedFile1 = fileRepository.save(defaultImage);
                        createFilmUniverse("영화 게시글" + movieTag, category, movieTag, "내용입니다", false, LocalDateTime.now(), LocalDateTime.now(), savedFile1.getId(), account);
                    }
                    break;
                case CRITIC:
                    movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                            .filter(tag -> tag.getCategory() == category)
                            .toList();
                    for (Tag movieTag : movieTagList) {
                        File defaultImage = File.builder() //게시글과 매핑될 디폴트 이미지 생성
                                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                                .originalFileName("original_file_name")
                                .build();
                        File savedFile2 = fileRepository.save(defaultImage);
                        createCritic("영화 게시글" + movieTag, category, movieTag, "내용입니다", false, savedFile2.getId(), account);
                    }
                    break;


            }
        }


    }

    private void createMovie(String title, Category category, Tag tag, String content, boolean containsImage, Account account) {
        ArticleCreateRequestDto movieRequestDto = new ArticleCreateRequestDto(title, category, tag, content);
        articleService.createArticle(movieRequestDto, AccountDto.from(account));
    }

    private void createFilmUniverse(String title, Category category, Tag tag, String content, boolean containsImage, LocalDateTime startAt, LocalDateTime endAt, Long mainImageId, Account account) {
        FilmUniverseCreateRequestDto filmUniverseCreateRequestDto
                = new FilmUniverseCreateRequestDto(title, category, tag, content, startAt, endAt);
        articleService.createArticle(filmUniverseCreateRequestDto, AccountDto.from(account));
    }

    private void createCritic(String title, Category category, Tag tag, String content, boolean containsImage, Long mainImageId, Account account) {
        CriticCreateRequestDto criticCreateRequestDto = new CriticCreateRequestDto(title, category, tag, content);
        articleService.createArticle(criticCreateRequestDto, AccountDto.from(account));

    }

    private void createComment(Account account) {

        List<Category> categorys = Arrays.stream(Category.values()).toList();
        Pageable pageable = PageRequest.of(0, 20);

        for (Category category : categorys) {

            // 세부 게시판 좋아요 테스트
            List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                    .filter(tag -> tag.getCategory() == category)
                    .toList();
            for (Tag movieTag : movieTagList) {
                // 태그 있는 경우의 응답
                Page<? extends ParentBoardListResponseDto> articleList = articleService.getBoardList(category, movieTag, pageable);
                for (ParentBoardListResponseDto article : articleList) {
                    CommentCreateResponseDto commentCreateResponseDto = createParentComment(article.getId(), "부모댓글", false, account);
                    createChildComment(article.getId(), commentCreateResponseDto.getCommentId(), "자식댓글", false, account);

                }

            }
        }

    }

    private CommentCreateResponseDto createParentComment(Long articleId, String content, boolean isManagerComment, Account account) {
        CommentCreateRequestDto dto = new CommentCreateRequestDto(articleId, null, content, false);
        CommentCreateResponseDto comment = commentService.createComment(dto, AccountDto.from(account));
        return comment;
    }

    private void createChildComment(Long articleId, Long parentCommentId, String content, boolean isManagerComment, Account account) {
        CommentCreateRequestDto dto2 = new CommentCreateRequestDto(articleId, parentCommentId, content, false);
        commentService.createComment(dto2, AccountDto.from(account));
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void testAccount() {
        Account newAccount = getAccount();
        Assertions.assertEquals(newAccount.getEmail(), "test@naver.com");
        Assertions.assertEquals(newAccount.getNickname(), "nickname1223");
        Assertions.assertEquals(newAccount.getProfileImage(), defaultProfileImage.getDefaultProfileImage());

    }

    @Test
    @DisplayName("게시글 작성 테스트")
    public void articleGenerateTest() {
        // 5개의 게시글을 생성
        // 총 4개의 게시글에 좋아요 처리
        // 총 4개의 게시글에 1개씩 부모댓글 자식댓글 생성후
        // 계정 삭제 테스트

        Account newAccount = getAccount();
        createAllArticle(newAccount);
        List<Category> categorys = Arrays.stream(Category.values()).toList();
        Pageable pageable = PageRequest.of(0, 20);

        for (Category category : categorys) {
            // 메인페이지 테스트
            List<? extends ParentMainPageResponseDto> mainPageDtoList = articleService.getMainPageDtoList(category, 10);
            // 태그 갯수만큼 게시글 생성되었는지 체크
            Assertions.assertEquals(mainPageDtoList.size(),
                    Arrays.stream(Tag.values()).filter(tag -> tag.getCategory() == category).toList().size());


            // 세부 게시판 테스트
            Page<? extends ParentBoardListResponseDto> boardList = articleService.getBoardList(category, null, pageable);
            // 태그그 없이 카테고리만 응답시에는 태그 갯수만큼 게시글 존재여부 확인
            assertEquals(boardList.getTotalElements(), Arrays.stream(Tag.values()).filter(tag -> tag.getCategory() == category).toList().size());
            List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                    .filter(tag -> tag.getCategory() == category)
                    .toList();
            for (Tag movieTag : movieTagList) {
                // 태그 있는 경우의 응답
                Page<? extends ParentBoardListResponseDto> articleList = articleService.getBoardList(category, movieTag, pageable);
                assertEquals(articleList.getTotalElements(), 1);
            }
        }

    }

    @Test
    @DisplayName("Deleted 처리 후 게시글이 보이는지 테스트")
    public void articleDeleteTest() {
        Account newAccount = getAccount();
        createArticlesAndTryVoting(newAccount);

        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("pass");

        accountService.deleteAccount(deleteAccountRequestDto, AccountDto.from(newAccount));

        List<Category> categorys = Arrays.stream(Category.values()).toList();
        Pageable pageable = PageRequest.of(0, 20);

        for (Category category : categorys) {
            // 메인페이지 테스트
            List<? extends ParentMainPageResponseDto> mainPageDtoList = articleService.getMainPageDtoList(category, 10);
            // 0개 반환 확인
            Assertions.assertEquals(mainPageDtoList.size(), 0);


            // 세부 게시판 테스트
            Page<? extends ParentBoardListResponseDto> boardList = articleService.getBoardList(category, null, pageable);
            // 태그그 없이 카테고리만 응답시에는 태그 갯수만큼 게시글 존재여부 확인
            assertEquals(boardList.getTotalElements(), 0);
            List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                    .filter(tag -> tag.getCategory() == category)
                    .toList();
            for (Tag movieTag : movieTagList) {
                // 태그 있는 경우의 응답
                Page<? extends ParentBoardListResponseDto> articleList = articleService.getBoardList(category, movieTag, pageable);
                assertEquals(articleList.getTotalElements(), 0);
            }
        }


    }

    @Test
    @DisplayName("좋아요 테스트")
    public void voteTest() {
        Account newAccount = getAccount();
        createArticlesAndTryVoting(newAccount);

        List<Vote> votes = voteRepository.findAll().stream().toList();
        // 좋아요가 전부 눌렸는지 체크
        Assertions.assertEquals(votes.size(), Tag.values().length);


    }

    @Test
    @DisplayName("댓글 작성 테스트")
    public void commentTest() {
        Account account = getAccount();
        createArticlesAndTryVoting(account);
        createComment(account);
        int commentSize = 2 * (Tag.values().length); // 부모 & 자식댓글
        List<Comment> commentList = commentRepository.findByAuthor(account);
        Assertions.assertEquals(commentList.size(), commentSize);

    }

    @Test
    @DisplayName("실제 회원 탈퇴 테스트")
    public void deleteExpiredAccount() {
        Account account = getAccount();
        createArticlesAndTryVoting(account);
        createComment(account);
        String email = account.getEmail();
        Long id = account.getId();

        accountService.deleteExpiredAccount(account);
        Optional<Article> articleOptional = articleRepository.findById(id);
        // 모든 게시글 삭제 확인
        Assertions.assertEquals(articleOptional.isEmpty(), true);
        Optional<Account> emailAccount = accountRepository.findByEmail(email);
        // 모든 댓글 삭제 확인
        Optional<Comment> commentOptional = commentRepository.findById(id);
        Assertions.assertEquals(commentOptional.isEmpty(), true);
        // 회원 삭제 확인
        Assertions.assertEquals(emailAccount.isEmpty(), true);

    }


}