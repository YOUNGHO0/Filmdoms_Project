package com.filmdoms.community.article.repository;


import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.extra.Critic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CriticRepository extends JpaRepository<Critic, Long> {

    @Query("SELECT c FROM Critic c " +
            "LEFT JOIN FETCH c.article " +
            "LEFT JOIN FETCH c.article.author " +
            "LEFT JOIN FETCH c.article.content where c.article.status = 'ACTIVE'")
    List<Critic> findAllWithArticleAuthorContent(Pageable pageable);

    Optional<Critic> findByArticleId(Long articleId);

    @Query(value = "SELECT c FROM Critic c " +
            "LEFT JOIN FETCH c.article " +
            "LEFT JOIN FETCH c.article.author " +
            "LEFT JOIN FETCH c.article.content where c.article.status = 'ACTIVE'"
            , countQuery = "select count(c) from Critic c where c.article.status = 'ACTIVE'")
    Page<Critic> getCritics(Pageable pageable);

    @Query(value = "SELECT c FROM Critic c " +
            "LEFT JOIN FETCH c.article " +
            "LEFT JOIN FETCH c.article.author " +
            "LEFT JOIN FETCH c.article.content where c.article.tag =:tagId and c.article.status = 'ACTIVE'"
            , countQuery = "SELECT count(c) from Critic c inner join c.article where c.article.tag =:tagId and c.article.status = 'ACTIVE'")
    Page<Critic> getCriticsByTag(@Param("tagId") Tag tag, Pageable pageable);

    @Query("SELECT c FROM Critic c " +
            "LEFT JOIN FETCH c.article " +
            "WHERE c.article.id = :articleId")
    Optional<Critic> findByArticleIdWithArticle(@Param("articleId") Long articleId);
}
