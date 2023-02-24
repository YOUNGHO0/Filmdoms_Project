package com.filmdoms.community.imagefile.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.imagefile.data.dto.response.UploadedFileResponseDto;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3UploadService amazonS3UploadService;

    @PostMapping("/api/v1/fileupload")
    public Response<UploadedFileResponseDto> uploadFile(@RequestParam("images") MultipartFile multipartFile)
            throws IOException {

        return Response.success(UploadedFileResponseDto.from(
                amazonS3UploadService.upload(multipartFile, multipartFile.getOriginalFilename())));
    }
}
