package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

//    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

//    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT a FROM Account a " +
            "LEFT JOIN FETCH a.profileImage " +
            "WHERE a.email = :email")
    Optional<Account> findByEmailWithImage(@Param("email") String email);

    @Query("SELECT a FROM Account a " +
            "where a.accountStatus = 'DELETED' ")
    Optional<List<Account>> findDeletedAccounts();

}
