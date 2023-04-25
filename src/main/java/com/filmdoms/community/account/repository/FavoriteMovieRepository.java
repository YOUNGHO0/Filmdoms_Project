package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {

    @Query("SELECT m FROM FavoriteMovie m " +
            "JOIN FETCH m.account " +
            "JOIN FETCH m.movie " +
            "WHERE m.account = :account")
    List<FavoriteMovie> findAllByAccount(@Param("account") Account account);
}
