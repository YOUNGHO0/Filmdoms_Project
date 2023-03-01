package com.filmdoms.community.imagefile.data.dto.response;

import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import lombok.Getter;

@Getter
public class ImageResponseDto {

    private Long id;
    private String uuidFileName;

    public ImageResponseDto(ImageFile imageFile) {
        this.id = imageFile.getId();
        this.uuidFileName = imageFile.getUuidFileName();
    }
}
