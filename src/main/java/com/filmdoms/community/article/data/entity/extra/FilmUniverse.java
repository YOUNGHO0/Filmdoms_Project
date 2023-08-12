package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilmUniverse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Article article;

    private String mainImage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    private FilmUniverse(Article article, String mainImage, LocalDateTime startDate, LocalDateTime endDate) {
        if (article.getCategory() != Category.FILM_UNIVERSE || article.getTag().getCategory() != Category.FILM_UNIVERSE) {
            throw new IllegalArgumentException("Wrong article category or tag");
        }
        this.article = article;
        this.mainImage = mainImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String mainImage, LocalDateTime startDate, LocalDateTime endDate) {
        this.mainImage = mainImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
