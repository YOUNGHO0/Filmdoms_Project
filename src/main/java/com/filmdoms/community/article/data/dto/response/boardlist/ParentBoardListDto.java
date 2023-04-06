package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import lombok.Getter;
import java.time.LocalDate;
@Getter
public abstract class ParentBoardListDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private SimpleAccountResponseDto author;
    private LocalDate dateCreated;
    private int view;
    private int vote_count;
    private long commentCount;

    public ParentBoardListDto(Article article)
    {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag =article.getTag();
        this.title = article.getTitle();
        this.author = SimpleAccountResponseDto.from(article.getAuthor());
        this.dateCreated = LocalDate.from(article.getDateCreated());
        this.view = article.getView();
        this.vote_count = article.getVoteCount();
        this.commentCount = article.getCommentCount();
    }

}
