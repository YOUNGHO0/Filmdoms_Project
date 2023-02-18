package com.filmdoms.community.review.repository;

import com.filmdoms.community.review.data.entity.MovieReviewHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieReviewHeaderRepository extends JpaRepository<MovieReviewHeader, Long> {

    public List<MovieReviewHeader> findTop5ByOrderByDateCreatedDesc();
}
