package com.filmdoms.community.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.data.DefaultProfileImage;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.OAuthType;
import com.filmdoms.community.account.data.dto.response.OAuthResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.RefreshTokenRepository;
import com.filmdoms.community.account.service.AccountService;
import com.filmdoms.community.account.service.AccountStatusCheck;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.config.jwt.JwtTokenProvider;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    /*
    The default Authorization Response baseUri (redirection endpoint) is /login/oauth2/code/*
    You also need to ensure the ClientRegistration.redirectUriTemplate matches the custom Authorization Response baseUri -> 프론트/백 나눠서 처리하므로 달라야 함
    */
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final DefaultProfileImage defaultProfileImage;
    private final ArticleRepository articleRepository;
    private final AccountService accountService;
    private final AccountStatusCheck accountStatusCheck;

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }

        try {
            String email = resolveEmailFromAuthentication(oAuth2AuthenticationToken);
            Account account = accountRepository.findByEmail(email)
                    .orElseGet(() -> createGuestAccountWithEmail(email)); //가입된 이메일이 아닌 경우 GUEST 등급의 Account 생성
            checkSocialLoginAccount(account); //소셜 로그인 계정 여부 확인
            accountStatusCheck.checkAccountStatus(account);

            String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(account.getId()));
            ResponseCookie refreshTokenCookie = resolveRefreshTokenCookieFromAccount(account);
            OAuthType oAuthType = resolveOAuthTypeFromAccountRole(account.getAccountRole());
            OAuthResponseDto responseDto = OAuthResponseDto.from(oAuthType, accessToken);

            //Response 객체에 응답을 세팅
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    Response.success(responseDto)
                            )
                    );
        } catch (ApplicationException e) {
            createApplicationExceptionResponse(response, e);
        }
    }

    private void createApplicationExceptionResponse(HttpServletResponse response, ApplicationException e) throws IOException {
        response.setStatus(e.getErrorCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(
                                Response.error(e.getErrorCode().name())
                        )
                );
    }

    private ResponseCookie resolveRefreshTokenCookieFromAccount(Account account) {
        String refreshTokenKey = String.valueOf(account.getId());
        String refreshToken = refreshTokenRepository.findByKey(refreshTokenKey)
                .orElseGet(() -> jwtTokenProvider.createRefreshToken(refreshTokenKey));
        refreshTokenRepository.save(refreshTokenKey, refreshToken);
        return jwtTokenProvider.createRefreshTokenCookie(refreshToken);
    }

    private void checkSocialLoginAccount(Account account) {
        if (!account.isSocialLogin()) { //소셜 로그인 계정이 아니면 예외 발생 -> 이메일 인증 필수로 구현해야 함 (잘못 기재한 이메일 때문에 이메일 실소유자가 가입을 못하는 경우 방지)
            throw new ApplicationException(ErrorCode.NOT_SOCIAL_LOGIN_ACCOUNT);
        }
    }

    public String resolveEmailFromAuthentication(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String email = AttributeConverter.getEmail(registrationId, attributes); //이메일 정보만 추출
        return email;
    }

    private Account createGuestAccountWithEmail(String email) {
        Account account = Account.builder()
                .email(email)
                .role(AccountRole.GUEST)
                .isSocialLogin(true)
                .profileImage(defaultProfileImage.getDefaultProfileImage())
                .build();
        return accountRepository.save(account);
    }

    private OAuthType resolveOAuthTypeFromAccountRole(AccountRole accountRole) {
        OAuthType oAuthType;
        switch (accountRole) {
            case GUEST:
                oAuthType = OAuthType.SIGNUP;
                break;
            default:
                oAuthType = OAuthType.LOGIN;
        }
        return oAuthType;
    }
}
