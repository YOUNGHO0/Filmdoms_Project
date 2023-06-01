package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.name IN :names")
    List<Movie> findAllByNames(@Param("names") List<String> names);
}
