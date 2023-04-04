package com.filmdoms.community.newcomment.repository;

import com.filmdoms.community.newcomment.data.entity.NewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewCommentRepository extends JpaRepository<NewComment, Long> {

    @Query("SELECT c FROM NewComment c " +
            "LEFT JOIN FETCH c.author.profileImage " +
            "WHERE c.article.id = :articleId")
    List<NewComment> findByArticleIdWithAuthorProfileImage(@Param("articleId") Long articleId);
}
