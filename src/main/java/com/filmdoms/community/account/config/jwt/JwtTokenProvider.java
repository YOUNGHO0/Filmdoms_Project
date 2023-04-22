package com.filmdoms.community.account.config.jwt;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.service.TokenAuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenAuthenticationService tokenAuthenticationService;


    @Value("${jwt.secret-key}")
    private String secretKey;
    private byte[] keyBytes;
    private final long TOKEN_VALID_MILLISECOND = 1000L * 60 * 60;

    @PostConstruct // Bean 으로 주입되면서 실행
    private void init() {
        keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String subject, Collection<? extends GrantedAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(subject);
        //claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_MILLISECOND))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 인증 정보 조회
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        AccountDto accountDto = tokenAuthenticationService.findAccountBySubject(getSubject(token));
        return new UsernamePasswordAuthenticationToken(accountDto, null, accountDto.getAuthorities());
    }

    // 토큰 기반 회원 구별 정보 추출
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // HTTP 헤더에서 Token 값 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 체크
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
