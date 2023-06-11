package com.filmdoms.community.account.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.config.jwt.JwtTokenProvider;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthFailureHandler implements AuthenticationFailureHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        createApplicationExceptionResponse(response, new ApplicationException(ErrorCode.SOCIAL_LOGIN_FAILED));
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
}
