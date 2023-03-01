package com.filmdoms.community.imagefile.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.imagefile.data.dto.response.ImageUploadResponseDto;
import com.filmdoms.community.imagefile.data.dto.response.UploadedFileResponseDto;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3UploadService amazonS3UploadService;

    @PostMapping("/fileupload")
    public Response<UploadedFileResponseDto> uploadFile(@RequestParam("images") MultipartFile multipartFile)
            throws IOException {

        return Response.success(UploadedFileResponseDto.from(
                amazonS3UploadService.upload(multipartFile, multipartFile.getOriginalFilename())));
    }

    @PostMapping("/image")
    public Response<ImageUploadResponseDto> uploadImages(
            @RequestPart("image") List<MultipartFile> imageMultipartFiles) {
        ImageUploadResponseDto responseDto = amazonS3UploadService.uploadAndSaveImages(imageMultipartFiles);
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
