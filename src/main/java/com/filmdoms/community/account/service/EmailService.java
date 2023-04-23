package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.request.AuthCodeVerificationRequestDto;
import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.service.utils.PasswordUtil;
import com.filmdoms.community.account.service.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final AccountRepository accountRepository;
    private final AsyncEmailSendService asyncEmailSendService;
    private final RedisUtil redisUtil;
    private static final long AUTH_CODE_EXPIRE_DURATION_IN_MILLISECONDS = 10 * 60 * 1000L;
    private static final String AUTH_CODE_KEY_PREFIX = "authCode:";

    public void sendTempPasswordEmail(String email) {
        Account account = findAccountByEmailAndVerify(email);
        String tempPassword = PasswordUtil.generateRandomPassword(10);
        account.updatePassword(tempPassword);
        String subject = "[필름덤즈] 임시 비밀번호를 발송해 드립니다.";
        String content = getTempPasswordEmailContent(tempPassword);
        asyncEmailSendService.sendEmail(email, subject, content, true, false);
    }

    public void sendAuthCodeEmail(String email) {
        findAccountByEmailAndVerify(email);
        String authCode = UUID.randomUUID().toString();
        redisUtil.setDataAndExpirationInMillis(AUTH_CODE_KEY_PREFIX + email, authCode, AUTH_CODE_EXPIRE_DURATION_IN_MILLISECONDS);
        String subject = "[필름덤즈] 인증 코드를 발송해 드립니다.";
        String content = getAuthEmailContent(authCode);
        asyncEmailSendService.sendEmail(email, subject, content, true, false);
    }

    public SimpleAccountResponseDto verityAuthCode(AuthCodeVerificationRequestDto requestDto) {
        String email = requestDto.getEmail();
        String authCode = requestDto.getAuthCode();
        Account account = findAccountByEmailAndVerify(email);
        String foundValue = redisUtil.getData(AUTH_CODE_KEY_PREFIX + email);
        if (foundValue == null || !foundValue.equals(authCode)) {
            throw new ApplicationException(ErrorCode.INVALID_AUTHENTICATION_CODE);
        }

        /*
        인증된 계정으로 전환하는 작업 수행
         */

        log.info("Account has been authenticated. email={}, nickname={}", email, account.getNickname());
        return SimpleAccountResponseDto.from(account);
    }

    private Account findAccountByEmailAndVerify(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_EMAIL));
        if (account.isSocialLogin()) {
            throw new ApplicationException(ErrorCode.SOCIAL_LOGIN_ACCOUNT);
        }
        return account;
    }

    private String getAuthEmailContent(String authCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='margin:20px;'>");
        sb.append("<p>필름덤즈 커뮤니티 인증 코드를 발송해 드립니다.</p>");
        sb.append("<br>");
        sb.append("<div align='center' style='border:1px solid black; font-family:verdana'>");
        sb.append("<h3>인증 코드</h3>");
        sb.append("<div style='font-size:130%'>");
        sb.append("<strong>");
        sb.append(authCode + "</strong></div><br/>");
        sb.append("</div></br></br>");
        sb.append("<p>감사합니다.</p>");
        sb.append("</div>");
        return sb.toString();
    }

    private String getTempPasswordEmailContent(String tempPassword) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='margin:20px;'>");
        sb.append("<p>필름덤즈 커뮤니티 임시 비밀번호를 발송해 드립니다.</p>");
        sb.append("<br>");
        sb.append("<div align='center' style='border:1px solid black; font-family:verdana'>");
        sb.append("<h3>임시 비밀번호</h3>");
        sb.append("<div style='font-size:130%'>");
        sb.append("<strong>");
        sb.append(tempPassword + "</strong></div><br/>");
        sb.append("</div></br></br>");
        sb.append("<p>로그인 후에 비밀번호를 꼭 변경해 주세요.<p>");
        sb.append("<p>감사합니다.</p>");
        sb.append("</div>");
        return sb.toString();
    }
}
