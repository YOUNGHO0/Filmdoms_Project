package com.filmdoms.community.file.data.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageUploadResponseDto {

    private List<Long> imageIds;

    public ImageUploadResponseDto(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
}
