package com.filmdoms.community.account.repository;

import com.filmdoms.community.account.data.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository  extends JpaRepository<ImageFile, Long> {
}
