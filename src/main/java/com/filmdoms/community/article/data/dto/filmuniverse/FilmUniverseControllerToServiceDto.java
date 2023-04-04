package com.filmdoms.community.article.data.dto.filmuniverse;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class FilmUniverseControllerToServiceDto extends ArticleControllerToServiceDto{


    private LocalDateTime startDate;
    private LocalDateTime endDate;


    public FilmUniverseControllerToServiceDto(String title, Category category, Tag tag, Account author,
                                              String content, Set<Long> contentImageId, LocalDateTime startDate, LocalDateTime endDate){
        super(title, category, tag, author, content, contentImageId);
        this.startDate =  startDate;
        this.endDate = endDate;

    }

    public static FilmUniverseControllerToServiceDto from (FilmUniverseRequestDto dto, Category category, Tag tag, Account author)
    {
        return FilmUniverseControllerToServiceDto.builder()
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
