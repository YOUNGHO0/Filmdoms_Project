package com.filmdoms.community.board.post.data.dto.response;

import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.PostDetailDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PostUpdateResponseDto {

    private Long id;

    public static PostUpdateResponseDto from(PostBriefDto dto) {
        return PostUpdateResponseDto.builder()
                .id(dto.getId())
                .build();
    }
}
