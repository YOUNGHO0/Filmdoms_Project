package com.filmdoms.community.board.notice.data.dto.response;

import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import lombok.Getter;

@Getter
public class NoticeUpdateResponseDto {

    private Long postId;

    public NoticeUpdateResponseDto(NoticeHeader header) {
        this.postId = header.getId();
    }
}
