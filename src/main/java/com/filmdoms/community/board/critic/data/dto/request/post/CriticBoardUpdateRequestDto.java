package com.filmdoms.community.board.critic.data.dto.request.post;

import lombok.Data;

import java.util.Set;

@Data
public class CriticBoardUpdateRequestDto {

    private Long id;
    private String preHeader;
    private String title;
    private String content;

    private Long mainImageId;
    private Set<Long> contentImageId;


}
