package com.filmdoms.community.file.data.dto.response;

import com.filmdoms.community.file.data.entity.File;
import lombok.Getter;

@Getter
public class FileDetailResponseDto {

    private Long id;
    private String originalFileName;
    private String uuidFileName;

    private FileDetailResponseDto(File file) {
        this.id = file.getId();
        this.originalFileName = file.getOriginalFileName();
        this.uuidFileName = file.getUuidFileName();
    }

    public static FileDetailResponseDto from(File file) {
        return new FileDetailResponseDto(file);
    }
}
