package com.filmdoms.community.article.data.entity;

import com.filmdoms.community.file.data.entity.FileContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", length = 10000)
    private String content;

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private Set<FileContent> fileContents = new HashSet<>();

    Content(String content) {
        this.content = content;
    }
}