package com.filmdoms.community.board.critic.data.dto.request.post;

import lombok.Data;

@Data
public class CriticBoardUpdateRequestDto {

    Long id;
    String preHeader;
    String title;
    String content;


}
