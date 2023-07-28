package com.filmdoms.community.comment.service;

import com.filmdoms.community.account.data.DefaultProfileImage;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import com.filmdoms.community.account.data.entity.Movie;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.FavoriteMovieRepository;
import com.filmdoms.community.account.repository.MovieRepository;
import com.filmdoms.community.account.service.AccountService;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ArticleCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.service.ArticleService;
import com.filmdoms.community.comment.data.dto.request.CommentCreateRequestDto;
import com.filmdoms.community.comment.data.dto.response.CommentCreateResponseDto;
import com.filmdoms.community.comment.data.dto.response.CommentResponseDto;
import com.filmdoms.community.comment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.comment.data.dto.response.ParentCommentResponseDto;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 댓글 삭제시 실제로 댓글 삭제 처리 되는지 테스트
// 부모 댓글중, 자식 댓글이 있는 댓글의 경우, 내용을 '삭제된 댓글입니다' 작성자를 null 처리 시
// 문제가 발생하는지 확인하는 테스트
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@DisplayName("댓글 삭제 테스트")
class CommentServiceTest2 {

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
        ArticleCreateRequestDto movieRequestDto = new ArticleCreateRequestDto(title, category, tag, content, containsImage);
        articleService.createArticle(movieRequestDto, AccountDto.from(account));
    }

    private void createFilmUniverse(String title, Category category, Tag tag, String content, boolean containsImage, LocalDateTime startAt, LocalDateTime endAt, Long mainImageId, Account account) {
        FilmUniverseCreateRequestDto filmUniverseCreateRequestDto
                = new FilmUniverseCreateRequestDto(title, category, tag, content, containsImage, startAt, endAt, mainImageId);
        articleService.createArticle(filmUniverseCreateRequestDto, AccountDto.from(account));
    }

    private void createCritic(String title, Category category, Tag tag, String content, boolean containsImage, Long mainImageId, Account account) {
        CriticCreateRequestDto criticCreateRequestDto = new CriticCreateRequestDto(title, category, tag, content, containsImage, mainImageId);
        articleService.createArticle(criticCreateRequestDto, AccountDto.from(account));

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
    private Account getAccount2() {
        // 계정 생성
        List<String> favMovie = List.of("토르", "스파이더맨", "영화123");
        JoinRequestDto requestDto = new JoinRequestDto("test2@naver.com", "pass", "nicknam23", favMovie, "tesmailuuid");

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
    private void createParentTestComment(Account account, Account account2) {

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
                    CommentCreateResponseDto commentCreateResponseDto = createParentComment(article.getId(), "2번째 계정이 만든 부모 댓글", false, account2);
                    createChildComment(article.getId(), commentCreateResponseDto.getCommentId(), "2번째 계정이 만든 댓글", false, account2);
                    createChildComment(article.getId(), commentCreateResponseDto.getCommentId(), "자식댓글", false, account);

                }

            }
        }

    }



    private void deleteParentComment(Account account2){

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

                    DetailPageCommentResponseDto detailPageCommentList = commentService.getDetailPageCommentList(article.getId());
                    List<ParentCommentResponseDto> comments = detailPageCommentList.getComments();
                    for(ParentCommentResponseDto parentComment : comments)
                    {
                        if ( parentComment.getAuthor().getId() == account2.getId()){
                            commentService.deleteComment(parentComment.getId(),AccountDto.from(account2));
                        }
                    }
                }

            }
        }
    }



    @Test
    @DisplayName("부모 댓글 삭제시 삭제된 댓글입니다. 작성자는 null 로 처리되는지 확인")
    public void parentCommentMaskTest() {

        Account account = getAccount();
        Account account2 = getAccount2();

        createAllArticle(account);
        createParentTestComment(account,account2);
        deleteParentComment(account2);

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

                    DetailPageCommentResponseDto detailPageCommentList = commentService.getDetailPageCommentList(article.getId());
                    List<ParentCommentResponseDto> comments = detailPageCommentList.getComments();
                    for(ParentCommentResponseDto parentComment : comments)
                    {
                        // 부모 댓글이 삭제된 댓글인지 확인한다.
                        Assertions.assertEquals("삭제된 댓글입니다.",parentComment.getContent());

                    }
                }

            }
        }

    }

    @Test
    @DisplayName("자식 댓글 삭제시, 부모 댓글이 삭제된 상황이면 같이 삭제되는지 확인")
    public void removeWithParentCommentTest()
    {
        //Account2로 부모댓글 자식댓글을 먼저 만들고
        // 그 뒤에 Account 로 자식 댓글을 하나 추가한다.
        // Account2 의 부모댓글을 삭제하고
        // Account2 의 자식 댓글을 삭제한다.
        // 이때 최종적으로 Account 댓글이 지워지면서 댓글이 전부 삭제되었는지 확인한다.
        Account account = getAccount();
        Account account2 = getAccount2();

        createAllArticle(account);
        createParentTestComment(account,account2);
        deleteParentComment(account2);

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

                    DetailPageCommentResponseDto detailPageCommentList = commentService.getDetailPageCommentList(article.getId());
                    List<ParentCommentResponseDto> comments = detailPageCommentList.getComments();
                    for(ParentCommentResponseDto parentComment : comments)
                    {
                        List<CommentResponseDto> childComments = parentComment.getChildComments();
                        for(CommentResponseDto childComment : childComments)
                        {
                            if (childComment.getAuthor().getId() == account2.getId())

                                commentService.deleteComment(childComment.getId(),AccountDto.from(account2));
                        }

                    }

                    for(ParentCommentResponseDto parentComment : comments)
                    {
                        List<CommentResponseDto> childComments = parentComment.getChildComments();
                        for(CommentResponseDto childComment : childComments)
                        {
                            if (childComment.getAuthor().getId() == account.getId())

                                commentService.deleteComment(childComment.getId(),AccountDto.from(account));
                        }

                    }


                }

            }
        }


        Assertions.assertEquals(0,commentRepository.findAll().size());
    }


}