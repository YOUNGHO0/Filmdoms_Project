package com.filmdoms.community.banner.repository;

import com.filmdoms.community.banner.data.entity.Banner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByOrderByIdDesc();

    @Query("SELECT b FROM Banner b " +
            "LEFT JOIN FETCH b.file " +
            "WHERE b.id = :bannerId")
    Optional<Banner> findByIdWithFile(long bannerId);
}
