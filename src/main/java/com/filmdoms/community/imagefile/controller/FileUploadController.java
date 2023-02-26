package com.filmdoms.community.imagefile.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.imagefile.data.dto.response.ImageUploadResponseDto;
import com.filmdoms.community.imagefile.data.dto.response.UploadedFileResponseDto;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    public Response<ImageUploadResponseDto> uploadImages(@RequestPart("image") List<MultipartFile> imageMultipartFiles) {
        ImageUploadResponseDto responseDto = amazonS3UploadService.uploadAndSaveImages(imageMultipartFiles);
        return Response.success(responseDto);
    }
}
