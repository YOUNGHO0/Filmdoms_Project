package com.filmdoms.community.file.data.dto.response;

import com.filmdoms.community.file.data.entity.File;
import lombok.Getter;

import java.util.List;

@Getter
public class FileUploadResponseDto {

    private List<FileDetailResponseDto> uploadedFiles;

    private FileUploadResponseDto(List<File> files) {
        this.uploadedFiles = files.stream()
                .map(FileDetailResponseDto::from)
                .toList();
    }

    public static FileUploadResponseDto from(List<File> files) {
        return new FileUploadResponseDto(files);
    }
}
