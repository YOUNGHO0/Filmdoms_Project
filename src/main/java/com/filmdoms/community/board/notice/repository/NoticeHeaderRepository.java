package com.filmdoms.community.board.notice.repository;

import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeHeaderRepository extends JpaRepository<NoticeHeader, Long> {

    @Query("SELECT h FROM NoticeHeader h " +
            "LEFT JOIN FETCH h.mainImage")
    List<NoticeHeader> findTopWithMainImage(Pageable pageable);

    @Query("SELECT h FROM NoticeHeader h " +
            "LEFT JOIN FETCH h.author " +
            "LEFT JOIN FETCH h.boardContent " +
            "LEFT JOIN FETCH h.mainImage " +
            "LEFT JOIN FETCH h.boardContent.imageFiles " +
            "WHERE h.id = :headerId")
    Optional<NoticeHeader> findByIdWithAuthorBoardContentMainImageContentImages(@Param("headerId") Long headerId);
}
