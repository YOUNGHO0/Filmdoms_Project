package com.filmdoms.community.board.review.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.review.data.dto.request.MovieReviewCreateRequestDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewCreateResponseDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.review.repository.MovieReviewHeaderRepository;
import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTestWithJpaAuditing
@DisplayName("영화 리뷰 서비스-리포지토리 통합 테스트")
class MovieReviewServiceTest {

    @SpyBean
    MovieReviewService movieReviewService;

    @SpyBean
    ImageFileService imageFileService;

    @MockBean //나중에 ImageFileService에 AmazonS3UploadService 의존성 사라지면 삭제
    AmazonS3UploadService amazonS3UploadService;

    @Autowired
    MovieReviewHeaderRepository headerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ImageFileRepository imageFileRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 이미지_없는_영화리뷰_생성() {
        //given
        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.ADMIN));
        AccountDto testAccountDto = AccountDto.from(testUser); //컨트롤러에서 받은 인증 객체 역할

        MovieReviewCreateRequestDto requestDto = new MovieReviewCreateRequestDto(MovieReviewTag.A, "영화 리뷰 제목", "영화 리뷰 내용", Collections.emptyList());

        //when
        MovieReviewCreateResponseDto responseDto = movieReviewService.create(requestDto, testAccountDto);

        //then
        MovieReviewHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getImageFiles().size()).isEqualTo(0); //이미지 없음
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId()); //request DTO에서 전달된 값들이 저장되었는지 확인
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(header.getTag()).isEqualTo(requestDto.getTag());
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    public void 이미지_있는_영화리뷰_생성() throws IOException {
        //given
        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.ADMIN));
        AccountDto testAccountDto = AccountDto.from(testUser); //컨트롤러에서 받은 인증 객체 역할

        //테스트 이미지 생성
        List<Long> imageIds = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            imageIds.add(createTestImage());
        }

        MovieReviewCreateRequestDto requestDto = new MovieReviewCreateRequestDto(MovieReviewTag.A, "영화 리뷰 제목", "영화 리뷰 내용", imageIds);

        //when
        MovieReviewCreateResponseDto responseDto = movieReviewService.create(requestDto, testAccountDto);
        em.flush();
        em.clear(); //리뷰 생성시에는 헤더에 이미지가 들어있지 않으므로 flush, clear 후 다시 불러와야 함

        //then
        assertThat(imageIds).size().isEqualTo(3);
        MovieReviewHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getImageFiles().size()).isEqualTo(imageIds.size()); //헤더에 ImageFile 객체 3개가 연결되었는지 확인
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(header.getTag()).isEqualTo(requestDto.getTag());
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
    }

    private Long createTestImage() { //테스트 이미지 데이터를 생성해서 저장하고 id를 반환
        ImageFile testImageFile = ImageFile.builder()
                .originalFileName("testOriginalFileName")
                .uuidFileName("testUuidFileName")
                .build();
        ImageFile savedImageFile = imageFileRepository.save(testImageFile);
        return savedImageFile.getId();
    }
}