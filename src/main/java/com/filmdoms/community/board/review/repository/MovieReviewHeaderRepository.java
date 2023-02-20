package com.filmdoms.community.board.review.repository;

import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieReviewHeaderRepository extends JpaRepository<MovieReviewHeader, Long> {

     List<MovieReviewHeader> findTop5ByOrderByDateCreatedDesc();


}
