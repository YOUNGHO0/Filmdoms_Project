package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.request.AuthCodeVerificationRequestDto;
import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.utils.RedisUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@SpringBootTest(classes = {EmailService.class})
@ActiveProfiles("test")
@DisplayName("이메일 서비스 테스트")
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @MockBean
    RedisUtil redisUtil;

    @MockBean
    AccountRepository accountRepository;

    @MockBean
    AsyncEmailSendService asyncEmailSendService;

    @Test
    @DisplayName("인증 코드가 일치하면 오류가 발생하지 않고 응답 DTO를 반환함")
    void verifyAuthCode() {
        //given
        String email = "address@domain.com";
        String authCode = "sampleAuthCode";
        String mockAccountNickname = "nickname";
        Account mockAccount = Account.builder()
                .nickname(mockAccountNickname)
                .email(email)
                .build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 1L, Long.class);
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.ofNullable(mockAccount));
        Mockito.when(redisUtil.getData(Mockito.any())).thenReturn(authCode);
        AuthCodeVerificationRequestDto requestDto = new AuthCodeVerificationRequestDto(email, authCode);

        //when
        SimpleAccountResponseDto responseDto = emailService.verityAuthCode(requestDto);

        //then
        Assertions.assertThat(responseDto.getId()).isEqualTo(1L);
        Assertions.assertThat(responseDto.getNickname()).isEqualTo(mockAccountNickname);
    }
}