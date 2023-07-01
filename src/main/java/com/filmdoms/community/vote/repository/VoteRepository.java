package com.filmdoms.community.vote.repository;

import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.vote.data.entity.Vote;
import com.filmdoms.community.vote.data.entity.VoteKey;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, VoteKey> {

    Optional<Vote> findByVoteKey(VoteKey voteKey);

    @Modifying
    @Query("DELETE FROM Vote v " +
            "WHERE v.voteKey.article = :article")
    void deleteByArticle(@Param("article") Article article);
}
