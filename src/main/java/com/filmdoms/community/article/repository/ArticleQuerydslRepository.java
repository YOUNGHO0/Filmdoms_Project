package com.filmdoms.community.article.repository;

import com.filmdoms.community.article.data.dto.response.boardlist.MovieListResponseResponseDto;
import com.filmdoms.community.article.data.entity.Article;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.filmdoms.community.account.data.entity.QAccount.*;
import static com.filmdoms.community.article.data.entity.QArticle.article;
import static com.filmdoms.community.article.data.entity.QContent.*;
import static com.filmdoms.community.file.data.entity.QFile.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ArticleQuerydslRepository {
    private final JPAQueryFactory query;

    public List<MovieListResponseResponseDto> getArticles(){

        List<Article> articleList = query
                .selectFrom(article)
                .join(article.author, account).fetchJoin()
                .join(account.profileImage, file).fetchJoin()
                .fetch();
        List<MovieListResponseResponseDto> collect = articleList.stream().map(MovieListResponseResponseDto::from).collect(Collectors.toList());
        return collect;
    }

    public Page<Article> getArticlePages(Pageable pageable){

        QueryResults<Article> articleQueryResults = query.select(article)
                .from(article)
                .join(article.author, account)
                .join(account.profileImage, file)
                .join(article.content, content1)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Article> results = articleQueryResults.getResults();
        for (Article result : results)
        {
            log.info(result.getId().toString());
        }
        long total = articleQueryResults.getTotal();

        return new PageImpl<>(results,pageable,total);
    }

}
