package com.filmdoms.community.account.repository;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(String key, String token);

    Optional<String> findByKey(String key);

    void deleteByKey(String key);

}
