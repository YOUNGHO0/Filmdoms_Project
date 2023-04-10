package com.filmdoms.community.article.data.dto.enuminfo;

import com.filmdoms.community.article.data.constant.Tag;
import lombok.Getter;

@Getter
public class TagListDto {

    Tag tag;
    String description;

    public TagListDto(Tag tag) {

        this.tag = tag;
        this.description = tag.getDescription();
    }

    public static TagListDto from(Tag tag) {
        return new TagListDto(tag);
    }
}
