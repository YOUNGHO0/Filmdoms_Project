package com.filmdoms.community.article.repository;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.PostStatus;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("Select a from Article a where a.category =:category and a.status ='ACTIVE'")
    List<Article> findByCategory(@Param("category") Category category, Pageable pageable);

    //JPA 기본 제공 findAll 메서드는 페이지 객체를 반환하므로 필요 없는 count 쿼리가 나감 -> 리스트를 반환하는 메서드를 따로 만듦
    @Query("SELECT a FROM Article a where a.status = 'ACTIVE'")
    List<Article> findAllReturnList(Pageable pageable);

    @Query("SELECT a FROM Article a " +
            "LEFT JOIN FETCH a.author.profileImage " +
            "LEFT JOIN FETCH a.content " +
            "WHERE a.id = :articleId and a.status ='ACTIVE' ")
    Optional<Article> findByIdWithAuthorProfileImageContent(@Param("articleId") Long articleId);

    // 게시판 리스트 페이지 조회
    @Query(value = "SELECT a from Article a inner join fetch a.author join fetch a.author.profileImage where a.category =:categoryId and a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) from Article a where a.category =: categoryId and a.status = 'ACTIVE'")
    Page<Article> findArticlesByCategory(@Param("categoryId") Category category, Pageable pageable);

    // 게시판 리스트 페이지에서 태그로 필터링
    @Query(value = "SELECT a from Article a inner join fetch a.author join fetch a.author.profileImage where a.category =:categoryId and a.tag =:tagId and a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) from Article a where a.category =: categoryId and a.tag =: tagId and a.status='ACTIVE'")
    Page<Article> findArticlesByCategoryAndTag(@Param("categoryId") Category category, @Param("tagId") Tag tag, Pageable pageable);

    // (Recent) 최근 게시물 조회
    @Query(value = "SELECT a from Article a inner join fetch  a.author join fetch  a.author.profileImage where a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) FROM  Article a where a.status = 'ACTIVE'")
    Page<Article> getAllArticles(Pageable pageable);

    // (Recent) 최근 게시물을 태그로 필터링
    @Query(value = "SELECT a from Article a inner join fetch  a.author join fetch  a.author.profileImage where a.tag =:tagId and a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) FROM  Article a where a.tag =:tagId and a.status = 'ACTIVE'")
    Page<Article> getAllArticlesByTag(@Param("tagId") Tag tag, Pageable pageable);

    // 인기 게시글 조회
    @Query(value = "SELECT a from Article a inner join fetch a.author where a.status = 'ACTIVE' order by a.voteCount desc limit 5")
    List<Article> getTop5Articles();

    // 제목 + 내용 으로 조회
    @Query(value = "SELECT a from Article a inner join fetch a.author inner join fetch a.author.profileImage inner join fetch a.content " +
            "where a.category =:categoryId and (a.title like %:keyword% or a.content.content like %:keyword%)and a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) from Article a inner join a.content where a.category =:categoryId and (a.title like %:keyword% or a.content.content like %:keyword%) and a.status = 'ACTIVE' ")
    Page<Article> findArticlesByKeyword(@Param("categoryId") Category category, @Param("keyword") String keyword, Pageable pageable);

    // 글작성자로 조회
    @Query(value = "SELECT a from Article a inner join fetch a.author join fetch a.author.profileImage where a.category =:categoryId and a.author.nickname =:nickname and a.status = 'ACTIVE'"
            , countQuery = "SELECT count(a) from Article a inner join a.author where a.category =:categoryId and a.author.nickname =:nickname and a.status = 'ACTIVE'")
    Page<Article> findArticlesByNickname(@Param("categoryId") Category category, @Param("nickname") String nickname, Pageable pageable);

    Page<Article> findByAuthor(Account author, Pageable pageable);

    List<Article> findByAuthor(Account account);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Article a SET a.status =:postStatus WHERE a.author =:author")
    void updateArticlesPostStatus(@Param("author") Account author, @Param("postStatus") PostStatus postStatus);

}
