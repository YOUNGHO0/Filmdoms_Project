package com.filmdoms.community.imagefile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3UploadService {

    public final ImageFileRepository imageFileRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public UploadedFileDto upload(MultipartFile multipartFile, String originalFileName) {

        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uuidFileName = UUID.randomUUID() + ext;

        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());
            amazonS3.putObject(bucket, uuidFileName, multipartFile.getInputStream(), objMeta);
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.S3_ERROR, e.getMessage());
        }

        log.info("업로드 성공");

        return UploadedFileDto.builder()
                .uuidFileName(uuidFileName)
                .originalFileName(originalFileName)
                .url(amazonS3.getUrl(bucket, uuidFileName).toString())
                .build();
    }
}