package com.filmdoms.community.account.exception;

import com.filmdoms.community.account.data.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        log.info(exception.getMessage() +
                " for request of URI: [" + request.getRequestURI() + "]" +
                " requested by Account: " + request.getUserPrincipal().getName());
        response.setContentType("application/json");
        response.setStatus(ErrorCode.AUTHORIZATION_ERROR.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.AUTHORIZATION_ERROR.name()).toStream());
    }
}

