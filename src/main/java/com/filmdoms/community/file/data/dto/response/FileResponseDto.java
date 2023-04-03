package com.filmdoms.community.file.data.dto.response;

import com.filmdoms.community.file.data.entity.File;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private Long id;
    private String uuidFileName;

    private FileResponseDto(Long id, String uuidFileName) {
        this.id = id;
        this.uuidFileName = uuidFileName;
    }

    public static FileResponseDto from(File file) {
        return new FileResponseDto(file.getId(), file.getUuidFileName());
    }
}