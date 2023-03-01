package com.filmdoms.community.imagefile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import com.filmdoms.community.imagefile.data.dto.response.ImageUploadResponseDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AmazonS3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    public final ImageFileRepository imageFileRepository;

    //to be deprecated
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

    public ImageUploadResponseDto uploadAndSaveImages(List<MultipartFile> imageMultipartFiles) {

        if (imageMultipartFiles == null) {
            throw new ApplicationException(ErrorCode.NO_IMAGE_ERROR); //발생할 가능성 없어 보임
        }

        List<Long> uploadedImageIds = new ArrayList<>();

        for (MultipartFile imageMultipartFile : imageMultipartFiles) {
            if (imageMultipartFile.isEmpty()) {
                throw new ApplicationException(ErrorCode.EMPTY_FILE_ERROR); //이미지가 비어 있으면 예외 발생
            }
            String originalFileName = imageMultipartFile.getOriginalFilename();
            String uuidFileName = getUuidFileName(originalFileName);
            uploadFile(imageMultipartFile, uuidFileName);

            ImageFile imageFile = ImageFile.builder()
                    .originalFileName(originalFileName)
                    .uuidFileName(uuidFileName)
                    .build();

            uploadedImageIds.add(imageFileRepository.save(imageFile).getId());
        }

        return new ImageUploadResponseDto(uploadedImageIds);
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
        imageFileRepository.save(ImageFile.builder()
                .originalFileName(originalFileName)
                .uuidFileName(uuidFileName)
                .build());
    }
}