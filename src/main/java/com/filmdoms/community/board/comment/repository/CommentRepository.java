package com.filmdoms.community.board.comment.repository;

import com.filmdoms.community.board.comment.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.author " +
            "WHERE c.header.id = :headerId")
    List<Comment> findByHeaderIdWithAuthor(@Param("headerId") Long headerId);
}
