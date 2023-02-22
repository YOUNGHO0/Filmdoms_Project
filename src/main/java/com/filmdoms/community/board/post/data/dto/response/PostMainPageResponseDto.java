package com.filmdoms.community.board.post.data.dto.response;

import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostMainPageResponseDto {

    private String category;
    private String title;
    private Integer commentsCount;

    public static PostMainPageResponseDto from(PostBriefDto dto) {
        return new PostMainPageResponseDto(
                dto.getPostCategory().toString(),
                dto.getTitle(),
                dto.getCommentCount()
        );
    }

}
