package com.filmdoms.community.board.post.repository;

import com.filmdoms.community.board.post.data.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

}
