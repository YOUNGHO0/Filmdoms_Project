package com.filmdoms.community.newcomment.repository;

import com.filmdoms.community.newcomment.data.entity.NewCommentVote;
import com.filmdoms.community.newcomment.data.entity.NewCommentVoteKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewCommentVoteRepository extends JpaRepository<NewCommentVote, NewCommentVoteKey> {

    Optional<NewCommentVote> findByVoteKey(NewCommentVoteKey voteKey);
}
