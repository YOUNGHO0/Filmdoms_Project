package com.filmdoms.community.article.data.dto.notice;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class NoticeControllerToServiceDto extends ArticleControllerToServiceDto{

    private String title;
    private Category category;
    private Tag tag;
    private Account author;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public NoticeControllerToServiceDto (String title, Category category, Tag tag, Account author,
                                         String content, LocalDateTime startDate, LocalDateTime endDate){
        super(title, category, tag, author, content);
        this.startDate =  startDate;
        this.endDate = endDate;

    }

    public static NoticeControllerToServiceDto from (NoticeRequestDto dto, Category category, Tag tag, Account author)
    {
        return NoticeControllerToServiceDto.builder()
                .title(dto.getTitle())
                .category(category)
                .tag(tag)
                .author(author)
                .content(dto.getContent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
