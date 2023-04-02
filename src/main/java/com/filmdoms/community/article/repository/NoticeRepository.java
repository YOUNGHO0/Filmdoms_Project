package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.entity.extra.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
