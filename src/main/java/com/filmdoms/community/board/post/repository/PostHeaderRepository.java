package com.filmdoms.community.board.post.repository;

import com.filmdoms.community.board.post.data.entity.PostHeader;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHeaderRepository extends JpaRepository<PostHeader, Long> {

    // 메인 페이지 호출시 사용
    List<PostHeader> findFirst4ByOrderByIdDesc();
}
