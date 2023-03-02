package com.filmdoms.community.board.critic.data.dto.request.post;

import lombok.Data;

import java.util.List;
import java.util.Set;


@Data
public class CriticBoardPostRequestDto {

    private String preHeader;
    private String title;
    private String content;
    private String author;
    private Long mainImageId;
    private Set<Long> contentImageId;


}
