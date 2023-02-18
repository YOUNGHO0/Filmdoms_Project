package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.review.MovieReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieReviewCommentRepository extends JpaRepository<MovieReviewComment, Long> {
}
