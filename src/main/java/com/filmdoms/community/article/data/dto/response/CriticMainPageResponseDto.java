package com.filmdoms.community.article.data.dto.response;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CriticMainPageResponseDto extends MainPageResponseDto {
    private FileResponseDto mainImage;
    private SimpleAccountResponseDto author;
    private String contentPreview;
    private LocalDate dateCreated;

    private CriticMainPageResponseDto(Long id, Category category, Tag tag, String title, FileResponseDto mainImage, SimpleAccountResponseDto author, String contentPreview, LocalDate dateCreated) {
        super(id, category, tag, title);
        this.mainImage = mainImage;
        this.author = author;
        this.contentPreview = contentPreview;
        this.dateCreated = dateCreated;
    }

    public static CriticMainPageResponseDto from(Critic critic) {
        Article article = critic.getArticle();
        return new CriticMainPageResponseDto(
                article.getId(), //article의 id를 반환
                article.getCategory(),
                article.getTag(),
                article.getTitle(),
                FileResponseDto.from(critic.getMainImage()),
                SimpleAccountResponseDto.from(article.getAuthor()),
                article.getContent().getContent(),
                article.getDateCreated().toLocalDate()
        );
    }
}
