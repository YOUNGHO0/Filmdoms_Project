package com.filmdoms.community.article.data.dto.response.boardlist;

import com.filmdoms.community.article.data.entity.extra.Announce;

public class AnnounceListResponseDto extends ParentBoardListResponseDto {

    public AnnounceListResponseDto(Announce announce) {
        super(announce.getArticle());
    }

    public static AnnounceListResponseDto from(Announce announce) {
        return new AnnounceListResponseDto(announce);
    }
}
