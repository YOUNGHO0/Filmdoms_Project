package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);
}
