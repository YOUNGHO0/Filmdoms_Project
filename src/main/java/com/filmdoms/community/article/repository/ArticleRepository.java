package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategory(Category category, Pageable pageable);
}
