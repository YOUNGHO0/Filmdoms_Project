package com.filmdoms.community.board.post.data.entity;

import com.filmdoms.community.board.data.BoardContentCore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("PostContent")
@Table(name = "\"post_content\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContent extends BoardContentCore {

    @Builder
    private PostContent(String content) {
        super(content);
    }

}
