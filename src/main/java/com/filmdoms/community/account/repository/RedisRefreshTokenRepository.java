package com.filmdoms.community.account.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final RedisTemplate<String, String> refreshTokenTemplate;
    private final static Duration TOKEN_TTL = Duration.ofDays(30);
    private final static String TOKEN_PREFIX = "REFRESH_TOKEN:";

    @Override
    public void save(String subject, String token) {
        String key = getKey(subject);
        refreshTokenTemplate.opsForValue().set(key, token, TOKEN_TTL);
    }

    @Override
    public Optional<String> findByKey(String subject) {
        String key = getKey(subject);
        String token = refreshTokenTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    @Override
    public void deleteByKey(String subject) {
        String key = getKey(subject);
        refreshTokenTemplate.delete(key);
    }

    private String getKey(String subject) {
        return TOKEN_PREFIX + subject;
    }
}
