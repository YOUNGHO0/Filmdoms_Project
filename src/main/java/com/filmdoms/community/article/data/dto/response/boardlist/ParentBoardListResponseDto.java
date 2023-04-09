package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class ParentBoardListResponseDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private SimpleAccountResponseDto author;
    private long createdAt;
    private int view;
    private int voteCount;
    private long commentCount;

    public ParentBoardListResponseDto(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.author = SimpleAccountResponseDto.from(article.getAuthor());
        this.createdAt = ZonedDateTime.of(article.getDateCreated(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.view = article.getView();
        this.voteCount = article.getVoteCount();
        this.commentCount = article.getCommentCount();
    }

}
