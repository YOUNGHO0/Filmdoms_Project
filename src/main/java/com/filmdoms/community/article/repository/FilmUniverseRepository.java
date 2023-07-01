package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FilmUniverseRepository extends JpaRepository<FilmUniverse, Long> {

    @Query("SELECT f FROM FilmUniverse f " +
            "LEFT JOIN FETCH f.article " +
            "LEFT JOIN FETCH f.article.author " +
            "LEFT JOIN FETCH f.mainImage")
    List<FilmUniverse> findAllWithArticleAuthorMainImage(Pageable pageable);
    @Query("SELECT f FROM FilmUniverse f " +
            "LEFT JOIN FETCH f.article " +
            "LEFT JOIN FETCH f.article.author.profileImage " +
            "LEFT JOIN FETCH f.article.content " +
            "WHERE f.article.id = :articleId")
    Optional<FilmUniverse> findByArticleIdWithArticleAuthorProfileImageContent(@Param("articleId") Long articleId);

    Optional<FilmUniverse> findByArticleId(Long articleId);

    @Query("SELECT f FROM FilmUniverse f " +
            "LEFT JOIN FETCH f.article " +
            "WHERE f.article.id = :articleId")
    Optional<FilmUniverse> findByArticleIdWithArticle(@Param("articleId") Long articleId);
}
