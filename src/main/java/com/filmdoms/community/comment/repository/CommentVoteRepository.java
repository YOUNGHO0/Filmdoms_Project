package com.filmdoms.community.comment.repository;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.data.entity.CommentVote;
import com.filmdoms.community.comment.data.entity.CommentVoteKey;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentVoteRepository extends JpaRepository<CommentVote, CommentVoteKey> {

    Optional<CommentVote> findByVoteKey(CommentVoteKey voteKey);

    @Modifying
    @Query("DELETE FROM CommentVote cv " +
            "WHERE cv.voteKey.comment IN :comments")
    void deleteByComments(@Param("comments") List<Comment> comments);

    @Modifying
    @Query("DELETE FROM CommentVote cv " +
            "WHERE cv.voteKey.account =:account")
    void deleteByAccount(@Param("account") Account account);

}
