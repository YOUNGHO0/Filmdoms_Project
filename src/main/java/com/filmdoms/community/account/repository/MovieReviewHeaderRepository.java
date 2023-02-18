package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.review.MovieReviewHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieReviewHeaderRepository extends JpaRepository<MovieReviewHeader, Long> {

    public List<MovieReviewHeader> findTop5ByOrderByDateCreatedDesc();
}
