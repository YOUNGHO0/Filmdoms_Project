package com.filmdoms.community.banner.repository;

import com.filmdoms.community.banner.data.entity.Banner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderByIdDesc();
}
