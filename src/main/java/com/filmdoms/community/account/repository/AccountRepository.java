package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a " +
            "LEFT JOIN FETCH a.profileImage " +
            "WHERE a.username = :username")
    Optional<Account> findByUsernameWithImage(String username);
}
