package com.filmdoms.community.review.repository;

import com.filmdoms.community.review.data.entity.MovieReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieReviewCommentRepository extends JpaRepository<MovieReviewComment, Long> {
}
