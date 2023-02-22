package com.filmdoms.community.board.notice.repository;

import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeHeaderRepository extends JpaRepository<NoticeHeader, Long> {

    List<NoticeHeader> findTop4ByOrderByDateCreatedDesc();
}
