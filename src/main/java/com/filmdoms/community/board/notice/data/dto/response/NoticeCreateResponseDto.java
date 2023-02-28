package com.filmdoms.community.board.notice.data.dto.response;

import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import lombok.Getter;

@Getter
public class NoticeCreateResponseDto {

    private Long postId;

    public NoticeCreateResponseDto(NoticeHeader header) {
        this.postId = header.getId();
    }
}
