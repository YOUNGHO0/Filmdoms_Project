package com.filmdoms.community.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.file.data.dto.response.FileUploadResponseDto;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AmazonS3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    public final FileRepository fileRepository;

    public FileUploadResponseDto uploadAndSaveFiles(List<MultipartFile> multipartFiles) {

        if (multipartFiles == null) {
            throw new ApplicationException(ErrorCode.NO_FILE_ERROR); //발생할 가능성 없어 보임
        }

        List<File> uploadedFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                throw new ApplicationException(ErrorCode.EMPTY_FILE_ERROR); //이미지가 비어 있으면 예외 발생
            }
            String originalFileName = multipartFile.getOriginalFilename();
            String uuidFileName = getUuidFileName(originalFileName);
            uploadFile(multipartFile, uuidFileName);

            File uploadedFile = File.builder().originalFileName(originalFileName)
                    .uuidFileName(uuidFileName)
                    .build();

            uploadedFiles.add(fileRepository.save(uploadedFile));
        }

        return FileUploadResponseDto.from(uploadedFiles);
    }

    private String getUuidFileName(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    private void uploadFile(MultipartFile multipartFile, String uuidFileName) {
        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getInputStream().available());
            amazonS3.putObject(bucket, uuidFileName, multipartFile.getInputStream(), objMeta);
            log.info("uploaded file: url={}", amazonS3.getUrl(bucket, uuidFileName));
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.S3_ERROR, e.getMessage());
        }
    }

    // TODO: 테스트 용도이므로 추후 삭제 예정
    public void createTestImage() {
        String originalFileName = "popcorn-movie-party-entertainment.webp";
        String uuidFileName = "3554e88f-d683-4f18-b3f4-33fbf6905792.webp";
        fileRepository.save(File.builder()
                .originalFileName(originalFileName)
                .uuidFileName(uuidFileName)
                .build());
    }
}