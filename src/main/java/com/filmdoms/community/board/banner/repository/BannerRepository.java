package com.filmdoms.community.board.banner.repository;

import com.filmdoms.community.board.banner.data.entity.BannerHeader;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<BannerHeader, Long> {

    List<BannerHeader> findAllByOrderByIdDesc();
}
