package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
