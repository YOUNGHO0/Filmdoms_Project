package com.filmdoms.community.account.service;

import com.filmdoms.community.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.request.OAuthJoinRequestDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import com.filmdoms.community.account.data.entity.Movie;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.FavoriteMovieRepository;
import com.filmdoms.community.account.repository.RefreshTokenRepository;
import com.filmdoms.community.account.service.utils.RedisUtil;
import com.filmdoms.community.testconfig.annotation.DataJpaTestWithJpaAuditing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTestWithJpaAuditing
@Import(AccountService.class)
@DisplayName("계정 서비스-리포지토리 통합 테스트")
class AccountServiceIntegrationTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FavoriteMovieRepository favoriteMovieRepository;

    @MockBean
    RefreshTokenRepository refreshTokenRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    RedisUtil redisUtil;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("소셜 로그인 계정의 회원가입 추가 정보가 알맞게 주어진 경우, 계정의 닉네임과 관심 영화가 업데이트되고, Role이 USER로 변경된다.")
    void socialLoginAccountInfoAddition() {
        //given
        Account account = Account.builder()
                .email("useremail@filmdoms.com")
                .role(AccountRole.GUEST)
                .isSocialLogin(true)
                .build();
        accountRepository.save(account);
        AccountDto accountDto = AccountDto.from(account);
        OAuthJoinRequestDto requestDto = new OAuthJoinRequestDto("nickname", List.of("MovieA", "MovieB", "MovieC"));

        //when
       // accountService.addInformationToSocialLoginAccount(requestDto, accountDto);
        em.flush();
        em.clear();

        //then
        Account findAccount = accountRepository.findById(account.getId()).get();
        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findAllByAccount(account);
        List<String> movieNameList = favoriteMovies.stream()
                .map(FavoriteMovie::getMovie)
                .map(Movie::getName)
                .toList();
        assertThat(findAccount.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(findAccount.getAccountRole()).isSameAs(AccountRole.USER);
        assertThat(favoriteMovies).size().isEqualTo(3);
        assertThat(movieNameList).containsExactlyInAnyOrderElementsOf(requestDto.getFavoriteMovies());
    }

}