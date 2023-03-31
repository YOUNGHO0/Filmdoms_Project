package com.filmdoms.community.article.data.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {
    MOVIE("영화게시판"), FILM_UNIVERSE("공지게시판"), CRITIC("비평게시판");

    private final String description;
}
