package com.filmdoms.community.account.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.filmdoms.community.account.data.entity.ImageFile;
import com.filmdoms.community.account.repository.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Upload {

    public final ImageFileRepository imageFileRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {
        String uuidFileName = UUID.randomUUID().toString() ;
        String originalFileName = multipartFile.getOriginalFilename();

        String s3FileName = uuidFileName+ "-" + originalFileName ;


        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        String fileUrl =  amazonS3.getUrl(bucket, s3FileName).toString();
        imageFileRepository.save(new ImageFile(uuidFileName,originalFileName,fileUrl));

        return fileUrl;
    }
}