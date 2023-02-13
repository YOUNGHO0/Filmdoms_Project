package com.filmdoms.community.board.review.repository;

import com.filmdoms.community.board.review.data.entity.MovieReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieReviewCommentRepository extends JpaRepository<MovieReviewComment, Long> {
}
