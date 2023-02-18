package com.filmdoms.community.postComment.repository;

import com.filmdoms.community.postComment.data.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

}
