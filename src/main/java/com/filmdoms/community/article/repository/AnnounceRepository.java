package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.extra.Announce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {

    @Query(value = "SELECT a from Announce a " +
            "inner join fetch a.article " +
            "inner join fetch a.article.author " +
            "inner join fetch a.article.author.profileImage"
            , countQuery = "SELECT count(a) from Announce a")
    Page<Announce> findAllAnnounceList(Pageable pageable);

    @Query(value = "SELECT a from Announce a " +
            "inner join fetch a.article " +
            "inner join fetch a.article.author " +
            "inner join fetch a.article.author.profileImage where a.article.category =:categoryId"
            , countQuery = "SELECT count(a) from Announce a inner join a.article where a.article.category =:categoryId")
    Page<Announce> findAnnounceListByCategory(@Param("categoryId") Category category, Pageable pageable);

    @Query(value = "Select a from Announce a where a.article.id =:announceId")
    Optional<Announce> findAnnounceByArticleId(@Param("announceId") Long id);
}
