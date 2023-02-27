package com.filmdoms.community.imagefile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.imagefile.data.dto.response.ImageUploadResponseDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTestWithJpaAuditing
@DisplayName("이미지 업로드 서비스-리포지토리 통합 테스트")
class AmazonS3UploadServiceTest {

    @Autowired
    ImageFileRepository imageFileRepository;

    @SpyBean
    AmazonS3UploadService amazonS3UploadService;

    @MockBean
    AmazonS3 amazonS3;

    @Test
    public void 이미지를_업로드하면_DB에_이미지_정보가_저장됨() {

        //given
        //가짜 멀티파트 이미지 5장 생성
        List<MultipartFile> imageMultipartFiles = IntStream.range(0, 5).boxed()
                .map(i -> (MultipartFile) new MockMultipartFile("image", "original_file_name_" + i + ".jpg",
                        "image/jpeg", new byte[10]))
                .collect(Collectors.toList());

        //가짜 행동 주입
        when(amazonS3.putObject(any(), any(), any(), any())).thenReturn(null);
        when(amazonS3.getUrl(any(), any())).thenReturn(null);

        //when
        ImageUploadResponseDto responseDto = amazonS3UploadService.uploadAndSaveImages(imageMultipartFiles);

        //then
        List<ImageFile> savedImageFiles = imageFileRepository.findAll();
        assertThat(savedImageFiles.size()).isEqualTo(5); //실제 이미지가 저장되는지 확인
        assertThat(responseDto.getImageIds().size()).isEqualTo(5); //응답 DTO가 잘 생성되는지 확인
    }

    @Test
    public void 비어_있는_이미지를_업로드하면_예외_발생() {

        //given
        MockMultipartFile emptyImage = new MockMultipartFile("image", "original_file_name.jpg",
                "image/jpeg", new byte[0]);
        List<MultipartFile> imageMultipartFiles = Arrays.asList(emptyImage);

        //when
        ApplicationException ex = assertThrows(ApplicationException.class, () -> amazonS3UploadService.uploadAndSaveImages(imageMultipartFiles));

        //then
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.EMPTY_FILE_ERROR);
    }
}