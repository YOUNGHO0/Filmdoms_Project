package com.filmdoms.community.imagefile.controller;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.imagefile.service.AmazonS3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3Upload amazonS3Upload;
    @PostMapping("/api/v1/fileupload")
    public Response<String> uploadFile(@RequestParam("images") MultipartFile multipartFile) throws IOException {

        String uuidFileName = UUID.randomUUID().toString() ;
        String originalFileName = multipartFile.getOriginalFilename();
        log.info("업로드 성공");
        return Response.success(amazonS3Upload.upload(multipartFile,uuidFileName,originalFileName)) ;

    }
}
