package com.filmdoms.community.board.post.data.dto.response;

import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostCreateResponseDto {

    private Long id;

    public static PostCreateResponseDto from(PostBriefDto dto) {
        return PostCreateResponseDto.builder()
                .id(dto.getId())
                .build();
    }
}
