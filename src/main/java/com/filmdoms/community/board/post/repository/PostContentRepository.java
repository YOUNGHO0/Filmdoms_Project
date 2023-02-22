package com.filmdoms.community.board.post.repository;

import com.filmdoms.community.board.post.data.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostContentRepository extends JpaRepository<PostContent, Long> {

}
