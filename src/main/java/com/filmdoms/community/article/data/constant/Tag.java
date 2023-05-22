package com.filmdoms.community.article.data.constant;

import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
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
    SUPPORTERS(Category.FILM_UNIVERSE, "대외활동"),
    COMPETITION(Category.FILM_UNIVERSE, "공모전"),
    CLUB(Category.FILM_UNIVERSE, "동아리"),
    CRITIC_ACTOR(Category.CRITIC, "배우비평"),
    CRITIC_DIRECTOR(Category.CRITIC, "감독비평"),
    CRITIC_MOVIE(Category.CRITIC, "영화비평");
    //에디터 태그를 만들어야 하는지 논의 필요

    private final Category category;
    private final String description;

    public void verifyCategory(Category category) {
        if (this.category != category) {
            throw new ApplicationException(ErrorCode.INVALID_TAG);
        }
    }

}
