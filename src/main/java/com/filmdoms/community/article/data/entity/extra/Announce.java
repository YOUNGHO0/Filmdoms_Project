package com.filmdoms.community.article.data.entity.extra;

import com.filmdoms.community.article.data.entity.Article;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Announce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JoinColumn(name = "article_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    Article article;

    @Builder
    public Announce(Article article) {
        this.article = article;
    }

    public static Announce from(Article article) {
        return new Announce(article);
    }
}
