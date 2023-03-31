package com.filmdoms.community.newcomment.repository;

import com.filmdoms.community.newcomment.data.entity.NewComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewCommentRepository extends JpaRepository<NewComment, Long> {
}
