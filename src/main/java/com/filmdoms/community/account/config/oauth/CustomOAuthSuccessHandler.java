package com.filmdoms.community.account.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.OAuthType;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.OAuthResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    /*
    The default Authorization Response baseUri (redirection endpoint) is /login/oauth2/code/*
    You also need to ensure the ClientRegistration.redirectUriTemplate matches the custom Authorization Response baseUri -> 프론트/백 나눠서 처리하므로 달라야 함
    */
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        //Provider로부터 받아온 정보로 회원가입/로그인 진행
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if (response.isCommitted()) {
            super.logger.debug("Response has already been committed.");
            return;
        }

        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        String email = AttributeConverter.getEmail(registrationId, attributes); //이메일 정보만 추출
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);

        if(optionalAccount.isPresent()) { //이메일과 일치하는 계정이 존재하는 경우
            Account account = optionalAccount.get();
            if(!account.isSocialLogin()) { //소셜 로그인 계정이 아니면 예외 발생 -> 이메일 인증 필수로 구현해야 함 (잘못 기재한 이메일 때문에 이메일 실소유자가 가입을 못하는 경우 방지)
                throw new ApplicationException(ErrorCode.NOT_SOCIAL_LOGIN_ACCOUNT);
            }

            //이메일과 일치하는 소셜 로그인 계정이 존재하는 경우는 2가지임
            //추가 정보 입력한 완전한 계정
            //아직 추가 정보를 입력하지 않은 계정 (GUEST 권한)
            //아래 5줄은 공통 처리
            //그 다음은 분기 처리
            AccountDto accountDto = AccountDto.from(account);
            String token = jwtTokenProvider.createToken(accountDto.getUsername(), accountDto.getAuthorities());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            if(accountDto.getAccountRole() != AccountRole.GUEST) {
                response.getWriter().write(objectMapper.writeValueAsString(Response.success(new OAuthResponseDto(OAuthType.LOGIN, token))));
            } else {
                response.getWriter().write(objectMapper.writeValueAsString(Response.success(new OAuthResponseDto(OAuthType.SIGNUP, token))));
            }
        } else { //
            Account newAccount = Account.builder()
                    .username(UUID.randomUUID().toString()) //임시로 UUID 발급 (JWT 인증절차 변경 필요)
                    .email(email)
                    .role(AccountRole.GUEST)
                    .isSocialLogin(true)
                    .build();

            accountRepository.save(newAccount);
            AccountDto accountDto = AccountDto.from(newAccount);
            String token = jwtTokenProvider.createToken(accountDto.getUsername(), accountDto.getAuthorities());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(Response.success(new OAuthResponseDto(OAuthType.SIGNUP, token))));
        }
    }
}
