package com.filmdoms.community.imagefile.repository;

import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository  extends JpaRepository<ImageFile, Long> , ImageFileRepositoryCustom {


}
