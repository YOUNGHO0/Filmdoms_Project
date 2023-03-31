package com.filmdoms.community.file.repository;

import com.filmdoms.community.file.data.entity.FileContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileContentRepository extends JpaRepository<FileContent, Long> {
}
