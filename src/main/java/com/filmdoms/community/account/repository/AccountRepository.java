package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

//    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

//    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT a FROM Account a " +
            "LEFT JOIN FETCH a.profileImage " +
            "WHERE a.email = :email")
    Optional<Account> findByEmailWithImage(String email);
}
