package com.filmdoms.community.comment.repository;

import com.filmdoms.community.comment.data.entity.CommentVote;
import com.filmdoms.community.comment.data.entity.CommentVoteKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentVoteRepository extends JpaRepository<CommentVote, CommentVoteKey> {

    Optional<CommentVote> findByVoteKey(CommentVoteKey voteKey);
}
