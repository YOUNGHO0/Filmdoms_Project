package com.filmdoms.community.comment.repository;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.data.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.author.profileImage " +
            "WHERE c.article.id = :articleId")
    List<Comment> findByArticleIdWithAuthorProfileImage(@Param("articleId") Long articleId);

    boolean existsByParentComment(Comment parentComment);

    @Query(value = "SELECT c FROM Comment c " +
            "LEFT JOIN FETCH c.article " +
            "WHERE c.author = :author",
            countQuery = "SELECT COUNT(c) FROM Comment c " +
                    "WHERE c.author = :author")
    Page<Comment> findByAuthorWithArticle(@Param("author") Account author, Pageable pageable);

    List<Comment> findByArticle(Article article);

    List<Comment> findByAuthor(Account account);

    List<Comment> findByParentComment(Comment parentComment);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Comment c SET c.status =:commentStatus WHERE c.author =:author")
    void updateCommentPostStatus(@Param("author") Account author, @Param("commentStatus") CommentStatus commentStatus);
}
