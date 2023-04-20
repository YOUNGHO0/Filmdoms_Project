package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.account.data.dto.response.DetailPageAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public abstract class ParentBoardListResponseDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private DetailPageAccountResponseDto writer;
    private long createdAt;
    private long updatedAt;
    private int views;
    private int likes;
    private long commentCount;
    private boolean isContainImage;
    public ParentBoardListResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.writer = DetailPageAccountResponseDto.from(article.getAuthor());
        this.createdAt = ZonedDateTime.of(article.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(article.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.views = article.getView();
        this.isContainImage = article.isContainsImage();
        this.likes = article.getVoteCount();
        this.commentCount = article.getCommentCount();
    }
}
