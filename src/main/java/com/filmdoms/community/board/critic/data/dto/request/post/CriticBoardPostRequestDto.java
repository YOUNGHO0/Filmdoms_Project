package com.filmdoms.community.board.critic.data.dto.request.post;

import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.constant.PostStatus;
import lombok.Data;


@Data
public class CriticBoardPostRequestDto {

    String preHeader;
    String title;
    String content;
    String author;


}
