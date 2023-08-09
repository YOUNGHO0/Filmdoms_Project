package com.filmdoms.community.article.data.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 50000)
    private String content;

    Content(String content) {
        this.content = content;
    }

    void updateContent(String content) {
        this.content = content;
    }
}