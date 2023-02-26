package com.filmdoms.community.board.review.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJPAAuditing;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.review.data.dto.request.MovieReviewCreateRequestDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewCreateResponseDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.review.repository.MovieReviewHeaderRepository;
import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTestWithJPAAuditing
@DisplayName("영화 리뷰 서비스-리포지토리 통합 테스트")
class MovieReviewServiceTest {

    @SpyBean
    MovieReviewService movieReviewService;

    @SpyBean
    ImageFileService imageFileService;

    @Autowired
    MovieReviewHeaderRepository headerRepository;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    AmazonS3UploadService amazonS3UploadService;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 이미지_없는_영화리뷰_생성() throws IOException {
        //given
        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.USER));
        MovieReviewCreateRequestDto requestDto = new MovieReviewCreateRequestDto(MovieReviewTag.A, "영화 리뷰 제목",
                testUser.getId(), "영화 리뷰 내용");

        //when
        MovieReviewCreateResponseDto responseDto = movieReviewService.createReview(requestDto, null);

        //then
        MovieReviewHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId()); //request DTO에서 전달된 값들이 저장되었는지 확인
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(header.getTag()).isEqualTo(requestDto.getTag());
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    public void 이미지_있는_영화리뷰_생성() throws IOException {
        //given
        UploadedFileDto uploadedFileDto = UploadedFileDto.builder()
                .uuidFileName("(randomUuidFileName).png")
                .build();
        //리뷰 이미지 생성
        List<MultipartFile> multipartFiles = IntStream.range(0, 5).boxed()
                .map(i -> (MultipartFile) new MockMultipartFile("image", "image_original_file_name_" + i, "image/jpeg",
                        new byte[10])) //파일 크기가 0이면 저장되지 않으므로 10바이트짜리 파일 생성
                .collect(Collectors.toList()); //가짜 멀티파트 이미지 5장 생성

        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.USER));
        MovieReviewCreateRequestDto requestDto = new MovieReviewCreateRequestDto(MovieReviewTag.A, "영화 리뷰 제목",
                testUser.getId(), "영화 리뷰 내용");
        Mockito.when(amazonS3UploadService.upload(any(), any()))
                .thenReturn(uploadedFileDto); //amazonS3Upload 객체에 가짜 행동 주입

        //when
        MovieReviewCreateResponseDto responseDto = movieReviewService.createReview(requestDto, multipartFiles);
        em.flush();
        em.clear(); //리뷰 생성시에는 헤더에 이미지가 들어있지 않으므로 flush, clear 후 다시 불러와야 함

        //then
        assertThat(multipartFiles).size().isEqualTo(5); //멀티파트 파일은 5개
        MovieReviewHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getImageFiles().size()).isEqualTo(multipartFiles.size()); //헤더에 ImageFile 객체 5개가 연결되었는지 확인
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(header.getTag()).isEqualTo(requestDto.getTag());
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
    }
}