package com.filmdoms.community.article.repository;


import com.filmdoms.community.article.data.entity.extra.Critic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CriticRepository extends JpaRepository<Critic, Long> {

    @Query("SELECT c FROM Critic c " +
            "LEFT JOIN FETCH c.article " +
            "LEFT JOIN FETCH c.article.author " +
            "LEFT JOIN FETCH c.mainImage " +
            "LEFT JOIN FETCH c.article.content")
    List<Critic> findAllWithArticleAuthorMainImageContent(Pageable pageable);
}
