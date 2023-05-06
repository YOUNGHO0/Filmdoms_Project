package com.filmdoms.community.account.service;

import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.DeleteAccountRequestDto;
import com.filmdoms.community.account.data.dto.request.JoinRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdatePasswordRequestDto;
import com.filmdoms.community.account.data.dto.request.UpdateProfileRequestDto;
import com.filmdoms.community.account.data.dto.response.AccountResponseDto;
import com.filmdoms.community.account.data.dto.response.LoginResponseDto;
import com.filmdoms.community.account.data.dto.response.RefreshAccessTokenResponseDto;
import com.filmdoms.community.account.data.dto.response.profile.ProfileArticleResponseDto;
import com.filmdoms.community.account.data.dto.response.profile.ProfileCommentResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import com.filmdoms.community.account.data.entity.Movie;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.FavoriteMovieRepository;
import com.filmdoms.community.account.repository.MovieRepository;
import com.filmdoms.community.account.repository.RefreshTokenRepository;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final FileRepository fileRepository;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MovieRepository movieRepository;
    private final FavoriteMovieRepository favoriteMovieRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public LoginResponseDto login(String email, String password) {

        log.info("가입 여부 확인");
        AccountDto accountDto = accountRepository.findByEmail(email)
                .map(AccountDto::from)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("비밀번호를 암호화 시켜 저장된 비밀번호와 대조");
        if (!passwordEncoder.matches(password, accountDto.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("저장된 토큰 존재 여부 확인, 없다면 생성");
        String key = UUID.nameUUIDFromBytes(email.getBytes()).toString();
        String refreshToken = refreshTokenRepository.findByKey(key)
                .orElseGet(() -> jwtTokenProvider.createRefreshToken(key));

        log.info("리프레시 토큰 저장 / 갱신");
        refreshTokenRepository.save(key, refreshToken);

        return LoginResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(String.valueOf(accountDto.getId())))
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public RefreshAccessTokenResponseDto refreshAccessToken(String refreshToken) {

        log.info("토큰 내 저장된 키 추출");
        String key = jwtTokenProvider.getSubject(refreshToken);

        log.info("키로 저장된 토큰 호출");
        String savedToken = refreshTokenRepository.findByKey(key)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_IN_DB));

        log.info("저장된 토큰과 대조");
        if (!Objects.equals(savedToken, refreshToken)) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }

        log.info("리프레시 토큰 갱신");
        refreshTokenRepository.save(key, refreshToken);

        log.info("새로운 엑세스 토큰 발급");
        String accessToken = jwtTokenProvider.createAccessToken(key);
        return RefreshAccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {

        log.info("토큰 내 저장된 키 추출");
        String key = jwtTokenProvider.getSubject(refreshToken);

        log.info("키로 저장된 토큰 호출");
        String savedToken = refreshTokenRepository.findByKey(key)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_IN_DB));

        log.info("저장된 토큰과 대조");
        if (!Objects.equals(savedToken, refreshToken)) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }

        log.info("저장된 토큰 삭제");
        refreshTokenRepository.deleteByKey(key);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    public boolean isEmailDuplicate(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Transactional
    public void createAccount(JoinRequestDto requestDto) {

        log.info("닉네임 중복 확인");
        if (isNicknameDuplicate(requestDto.getNickname())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_NICKNAME);
        }

        log.info("이메일 중복 확인");
        if (isEmailDuplicate(requestDto.getEmail())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
        }

        log.info("Account 엔티티 생성");
        Account newAccount = Account.builder()
                //.username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .email(requestDto.getEmail())
                .role(AccountRole.USER)
                .profileImage(getDefaultImage())
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
        favoriteMovieRepository.saveAll(favoriteMovies);
    }

    // TODO: 프로필 기본 이미지 어떻게 처리할 지 상의 필요
    private File getDefaultImage() {
        return fileRepository.findById(1L).orElseGet(() -> fileRepository.save(
                        File.builder()
                                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                                .originalFileName("original_file_name")
                                .build()
                )
        );
    }

    public AccountResponseDto readAccount(AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmailWithImage(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("선호 영화 호출");
        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findAllByAccount(account);

        return AccountResponseDto.from(account, favoriteMovies);
    }

    @Transactional
    public AccountResponseDto updateAccountProfile(UpdateProfileRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmailWithImage(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        File profileImage = account.getProfileImage();
        if (!Objects.equals(requestDto.getFileId(), profileImage.getId())) {
            log.info("요청 File 엔티티 호출");
            profileImage = fileRepository.findById(requestDto.getFileId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_FILE_ID));
        }

        log.info("Account 엔티티 수정");
        account.updateProfile(requestDto.getNickname(), profileImage);

        log.info("요청 관심 영화 호출 / 생성");
        List<FavoriteMovie> requestedFavoriteMovies = findOrCreateFavoriteMovies(requestDto.getFavoriteMovies()).stream()
                .map(movie -> FavoriteMovie.builder()
                        .movie(movie)
                        .account(account)
                        .build())
                .toList();

        log.info("기존 관심 영화 호출");
        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findAllByAccount(account);

        log.info("기존 관심 영화에 없는 엔티티 연결");
        requestedFavoriteMovies.stream()
                .filter(requestedFavoriteMovie -> !favoriteMovies.contains(requestedFavoriteMovie))
                .forEach(favoriteMovieRepository::save);

        log.info("요청 관심 영화에 없는 엔티티 삭제");
        favoriteMovies.stream()
                .filter(favoriteMovie -> !requestedFavoriteMovies.contains(favoriteMovie))
                .forEach(favoriteMovieRepository::delete);

        return AccountResponseDto.from(account, requestedFavoriteMovies);
    }

    @Transactional
    public void updateAccountPassword(UpdatePasswordRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("기존 비밀번호와 대조");
        if (!passwordEncoder.matches(requestDto.getOldPassword(), account.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("비밀번호 수정");
        account.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Transactional
    public void deleteAccount(DeleteAccountRequestDto requestDto, AccountDto accountDto) {

        log.info("Account 엔티티 호출");
        Account account = accountRepository.findByEmail(accountDto.getEmail())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        log.info("기존 비밀번호와 대조");
        if (!passwordEncoder.matches(requestDto.getPassword(), account.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        log.info("Account 엔티티 삭제");
        accountRepository.delete(account);
    }

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

    public ProfileArticleResponseDto getProfileArticles(Long accountId, Pageable pageable) {
        Account author = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Page<Article> articlePage = articleRepository.findByAuthor(author, pageable);
        return ProfileArticleResponseDto.from(articlePage);
    }

    public ProfileCommentResponseDto getProfileComments(Long accountId, Pageable pageable) {
        Account author = accountRepository.findById(accountId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Page<Comment> commentPage = commentRepository.findByAuthorWithArticle(author, pageable);
        return ProfileCommentResponseDto.from(commentPage);
    }
}
