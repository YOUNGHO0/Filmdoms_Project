package com.filmdoms.community.account.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("jwt 토큰 필터 호출");
            // 토큰 추출
            String token = jwtTokenProvider.resolveToken(request);

            // 토큰 유효성 체크
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                log.debug("Key is expired or signing algorithm does not match.");
                filterChain.doFilter(request, response);
                return;
            }
            log.info("토큰 유효성 체크 완료");
            // Authentication 부여
            UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(token);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("authentication 설정 완료");

        } catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
