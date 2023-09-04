package com.filmdoms.community.article.data.dto.response.mainpage;

import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import lombok.Getter;

/**
 * 메인 페이지의 영화 카테고리, recent 게시글 응답 DTO
 */
@Getter
public class MovieAndRecentMainPageResponseDto extends ParentMainPageResponseDto { //recent, movie 메인페이지
    private Long commentCount;

    private MovieAndRecentMainPageResponseDto(Article article) {
        super(article);
        this.commentCount = article.getComments()
                .stream().filter(comment -> comment.getStatus() == CommentStatus.ACTIVE).count();
    }

    public static MovieAndRecentMainPageResponseDto from(Article article) {
        return new MovieAndRecentMainPageResponseDto(article);
    }
}
