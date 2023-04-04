package com.filmdoms.community.file.repository;

import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.data.entity.FileContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileContentRepository extends JpaRepository<FileContent, Long> {

    Optional<FileContent> findByFile(File file);
}
