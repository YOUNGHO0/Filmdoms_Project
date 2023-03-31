package com.filmdoms.community.article.data.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Tag {
    MOVIE(Category.MOVIE, "영화"),
    OTT(Category.MOVIE, "OTT"),
    DRAMA(Category.MOVIE, "드라마"),
    EVENT(Category.MOVIE, "이벤트"),
    GOODS(Category.MOVIE, "굿즈"),
    OUTSIDE_ACTIVITY(Category.FILM_UNIVERSE, "대외활동"),
    COMPETITION(Category.FILM_UNIVERSE, "공모전"),
    CRITIC_ACTOR(Category.CRITIC, "배우비평"),
    CRITIC_DIRECTOR(Category.CRITIC, "감독비평"),
    CRITIC_MOVIE(Category.CRITIC, "영화비평");


    private final Category category;
    private final String description;
}
