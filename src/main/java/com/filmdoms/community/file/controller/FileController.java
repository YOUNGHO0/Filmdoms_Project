package com.filmdoms.community.file.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.file.service.AmazonS3UploadService;
import com.filmdoms.community.file.data.dto.response.FileUploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

    private final AmazonS3UploadService amazonS3UploadService;

    @PostMapping("/file")
    public Response<FileUploadResponseDto> upload(
            @RequestPart("file") List<MultipartFile> multipartFiles) {
        FileUploadResponseDto responseDto = amazonS3UploadService.uploadAndSaveFiles(multipartFiles);
        return Response.success(responseDto);
    }

    // TODO: 테스트 용도이므로 추후 삭제 예정
    // 실제 S3에는 업로드 안하고, DB에 매핑 안된 이미지를 추가해주는 API 입니다.
    @GetMapping("/test-image")
    public Response createTestImage() {
        amazonS3UploadService.createTestImage();
        return Response.success();
    }
}
