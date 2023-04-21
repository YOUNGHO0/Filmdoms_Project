package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.account.data.dto.response.DetailPageAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import com.filmdoms.community.file.data.entity.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 영화, 비평 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class ArticleDetailResponseDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private PostStatus status;
    private int views;
    private int likes;
    private boolean liked;
    private String content;
    private long createdAt;
    private long updatedAt;
    private DetailPageAccountResponseDto author;
    private List<FileResponseDto> images;

    protected ArticleDetailResponseDto(Article article, List<File> images, boolean isVoted) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.status = article.getStatus();
        this.views = article.getView();
        this.likes = article.getVoteCount();
        this.liked = isVoted;
        this.content = article.getContent().getContent();
        this.createdAt = ZonedDateTime.of(article.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(article.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.author = DetailPageAccountResponseDto.from(article.getAuthor());
        this.images = images.stream().sorted(Comparator.comparing(File::getId)).map(FileResponseDto::from).toList(); //id로 정렬한 뒤 DTO 변환
    }

    public static ArticleDetailResponseDto from(Article article, List<File> images, boolean isVoted) {
        return new ArticleDetailResponseDto(article, images, isVoted);
    }
}
