package com.filmdoms.community.board.post.repository;

import com.filmdoms.community.board.post.data.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 메인 페이지 호출시 사용
    List<Post> findFirst4ByOrderByIdDesc();
}
