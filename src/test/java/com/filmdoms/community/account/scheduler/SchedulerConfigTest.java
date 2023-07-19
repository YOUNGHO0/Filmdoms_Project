package com.filmdoms.community.account.scheduler;

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
import com.filmdoms.community.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class SchedulerConfigTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DefaultProfileImage defaultProfileImage;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;

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

    @Test
    void deleteExpiredAccount() {

        Account account = getAccount();
        DeleteAccountRequestDto deleteAccountRequestDto = new DeleteAccountRequestDto("pass");

        accountService.deleteAccount(deleteAccountRequestDto, AccountDto.from(account));
        account.updateStatusToDeleted(LocalDateTime.of(2023, 4, 3, 0, 0, 0));
        accountRepository.save(account);
        Optional<List<Account>> deletedAccounts = accountRepository.findDeletedAccounts();
        Assertions.assertEquals(deletedAccounts.isPresent(), true);
        Assertions.assertEquals(deletedAccounts.get().size(), 1);
        String email = account.getEmail();

        if (deletedAccounts.isPresent()) {
            List<Account> deletedList = deletedAccounts.get();
            deletedList.stream().filter(account1 -> {
                long daysBetween = ChronoUnit.DAYS.between(account1.getDeleteRegisteredDate(), LocalDateTime.now());
                return daysBetween >= 7;
            }).forEach(account1 -> accountService.deleteExpiredAccount(account1));
        }

        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertEquals(true, optionalAccount.isEmpty());

    }
}