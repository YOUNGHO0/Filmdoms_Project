package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Critic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "article_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Article article;

    private String mainImage;

    @Builder
    private Critic(Article article, String mainImage) {
        if (article.getCategory() != Category.CRITIC || article.getTag().getCategory() != Category.CRITIC) {
            throw new IllegalArgumentException("Wrong article category or tag");
        }
        this.article = article;
        this.mainImage = mainImage;
    }

    public void update(String mainImage) {
        this.mainImage = mainImage;
    }
}