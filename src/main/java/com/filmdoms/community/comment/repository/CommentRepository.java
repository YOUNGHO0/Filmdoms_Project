package com.filmdoms.community.comment.repository;

import com.filmdoms.community.comment.data.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.author.profileImage " +
            "WHERE c.article.id = :articleId")
    List<Comment> findByArticleIdWithAuthorProfileImage(@Param("articleId") Long articleId);

    boolean existsByParentComment(Comment parentComment);
}
