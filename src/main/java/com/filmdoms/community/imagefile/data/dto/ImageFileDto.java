package com.filmdoms.community.imagefile.data.dto;

import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ImageFileDto {


    private Long id;
    private String uuidFileName;
    private String fileUrl;
    private Long contentId;

    public static ImageFileDto from(ImageFile entity, String domain) {
        String fileUrl = domain + "/" + entity.getUuidFileName();

        return ImageFileDto.builder()
                .id(entity.getId())
                .uuidFileName(entity.getUuidFileName())
                .fileUrl(fileUrl)
                .contentId(entity.getBoardContent().getId())
                .build();
    }
}
