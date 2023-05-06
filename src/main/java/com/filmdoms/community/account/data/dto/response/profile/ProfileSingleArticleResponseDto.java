package com.filmdoms.community.account.data.dto.response.profile;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.PostStatus;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ProfileSingleArticleResponseDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private PostStatus status;
    private long createdAt;
    private long updatedAt;
    private int views;
    private int likes;
    private long commentCount;
    private boolean isContainImage;

    private ProfileSingleArticleResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.status = article.getStatus();
        this.createdAt = ZonedDateTime.of(article.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.updatedAt = ZonedDateTime.of(article.getDateLastModified(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.views = article.getView();
        this.likes = article.getVoteCount();
        this.commentCount = article.getCommentCount();
        this.isContainImage = article.isContainsImage();
    }

    public static ProfileSingleArticleResponseDto from(Article article) {
        return new ProfileSingleArticleResponseDto(article);
    }
}
