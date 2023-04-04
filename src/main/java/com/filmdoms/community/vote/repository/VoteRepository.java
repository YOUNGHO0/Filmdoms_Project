package com.filmdoms.community.vote.repository;

import com.filmdoms.community.vote.data.entity.Vote;
import com.filmdoms.community.vote.data.entity.VoteKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, VoteKey> {

    Optional<Vote> findByVoteKey(VoteKey voteKey);
}
