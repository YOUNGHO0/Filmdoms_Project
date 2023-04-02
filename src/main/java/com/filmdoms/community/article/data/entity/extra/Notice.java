package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.file.data.entity.File;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Article article;

    @JoinColumn(name = "file_id")
    @OneToOne(fetch = FetchType.LAZY)
    private File mainImage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    private Notice(Article article, File mainImage, LocalDateTime startDate, LocalDateTime endDate) {
        this.article = article;
        this.mainImage = mainImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Notice from(Article article, LocalDateTime startDate, LocalDateTime endDate) {
        return  Notice.builder()
                .article(article)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
