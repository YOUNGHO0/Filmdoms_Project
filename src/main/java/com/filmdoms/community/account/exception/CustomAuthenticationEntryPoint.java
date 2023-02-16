package com.filmdoms.community.account.exception;

import com.filmdoms.community.account.data.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements
        AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug(authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(ErrorCode.AUTHENTICATION_ERROR.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.AUTHENTICATION_ERROR.name()).toStream());
    }
}
