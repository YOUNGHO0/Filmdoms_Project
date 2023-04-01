package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.article.data.entity.Article;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@RequiredArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    @OneToOne
    @JoinColumn(name = "article_id")
    Article article;

    @Builder
    private Notice(Article article,LocalDateTime startDate, LocalDateTime endDate) {
        this.article = article;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Notice from(Article article, LocalDateTime startDate, LocalDateTime endDate)
    {
      return  Notice.builder()
                .article(article)
                .startDate(startDate)
                .endDate(endDate)
                .build();

    }


}
