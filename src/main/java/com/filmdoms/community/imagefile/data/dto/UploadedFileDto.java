package com.filmdoms.community.imagefile.data.dto;

import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UploadedFileDto {

    String originalFileName;
    String uuidFileName;
    String url;

    public ImageFile toEntity(BoardContent content) {
        return ImageFile.builder()
                .originalFileName(originalFileName)
                .uuidFileName(uuidFileName)
                .boardContent(content)
                .build();
    }
}
