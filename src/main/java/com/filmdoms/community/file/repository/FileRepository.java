package com.filmdoms.community.file.repository;

import com.filmdoms.community.file.data.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM Article a " +
            "JOIN a.content.fileContents fc " +
            "JOIN fc.file f " +
            "WHERE a.id = :articleId")
    List<File> findByArticleId(@Param("articleId") Long articleId);
}
