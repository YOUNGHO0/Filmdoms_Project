package com.filmdoms.community.file.repository;

import com.filmdoms.community.file.data.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {


}
