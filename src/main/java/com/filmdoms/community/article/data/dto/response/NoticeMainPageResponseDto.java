package com.filmdoms.community.article.data.dto.response;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Notice;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NoticeMainPageResponseDto extends MainPageResponseDto {
    private FileResponseDto mainImage;
    private SimpleAccountResponseDto author;
    private LocalDate startDate;
    private LocalDate endDate;

    private NoticeMainPageResponseDto(Long id, Category category, Tag tag, String title, FileResponseDto mainImage, SimpleAccountResponseDto author, LocalDate startDate, LocalDate endDate) {
        super(id, category, tag, title);
        this.mainImage = mainImage;
        this.author = author;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static NoticeMainPageResponseDto from(Notice notice) {
        Article article = notice.getArticle();
        return new NoticeMainPageResponseDto(
                article.getId(), //notice가 아닌 article의 id를 반환
                article.getCategory(),
                article.getTag(),
                article.getTitle(),
                FileResponseDto.from(notice.getMainImage()),
                SimpleAccountResponseDto.from(article.getAuthor()),
                notice.getStartDate().toLocalDate(),
                notice.getEndDate().toLocalDate());
    }
}
