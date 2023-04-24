package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmUniverseRepository extends JpaRepository<FilmUniverse, Long> {

    @Query("SELECT n FROM FilmUniverse n " +
            "LEFT JOIN FETCH n.article " +
            "LEFT JOIN FETCH n.article.author " +
            "LEFT JOIN FETCH n.mainImage")
    List<FilmUniverse> findAllWithArticleAuthorMainImage(Pageable pageable);
    @Query("SELECT n FROM FilmUniverse n " +
            "LEFT JOIN FETCH n.article " +
            "LEFT JOIN FETCH n.article.author.profileImage " +
            "LEFT JOIN FETCH n.article.content " +
            "WHERE n.article.id = :articleId")
    Optional<FilmUniverse> findByArticleIdWithArticleAuthorProfileImageContent(@Param("articleId") Long articleId);

    Optional<FilmUniverse> findByArticleId(Long articleId);
}
