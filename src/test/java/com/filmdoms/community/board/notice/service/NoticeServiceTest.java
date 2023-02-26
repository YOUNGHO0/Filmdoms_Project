package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.board.notice.repository.NoticeHeaderRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTestWithJpaAuditing
@DisplayName("공지 서비스-리포지토리 통합 테스트")
class NoticeServiceTest {

    @SpyBean
    NoticeService noticeService;

    @SpyBean
    ImageFileService imageFileService;

    @Autowired
    NoticeHeaderRepository headerRepository;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    AmazonS3UploadService amazonS3UploadService;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 공지는_메인_이미지_없으면_예외_발생() {
        //given
        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.ADMIN));

        //공지 시작일 종료일 설정
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);

        NoticeCreateRequestDto requestDto = new NoticeCreateRequestDto("공지 제목", testUser.getId(), "공지 내용", startDate,
                endDate);

        //when
        MultipartFile mainImageMultipartFile = null;

        //then
        ApplicationException ex = assertThrows(ApplicationException.class, () -> {
            noticeService.create(requestDto, mainImageMultipartFile, null);
        });
        assertThat(ex.getErrorCode().name()).isEqualTo("NO_MAIN_IMAGE_ERROR");
    }

    @Test
    public void 메인이미지_서브이미지_있는_공지_생성() throws IOException {

        //given
        UploadedFileDto uploadedFileDto = UploadedFileDto.builder()
                .uuidFileName("(randomUuidFileName).png")
                .build();
        Account testUser = accountRepository.save(Account.of("user1", "1234", AccountRole.ADMIN));

        //공지 시작일 종료일 설정
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);

        //공지 이미지 설정
        MultipartFile mainImageMultipartFile = new MockMultipartFile("image", "main_image_original_file_name",
                "image/jpeg", new byte[10]); //파일 크기가 0이면 저장되지 않으므로 10바이트짜리 파일 생성
        List<MultipartFile> subImageMultipartFiles = IntStream.range(0, 5).boxed()
                .map(i -> (MultipartFile) new MockMultipartFile("subImage", "sub_image_original_file_name_" + i,
                        "image/jpeg", new byte[10]))
                .collect(Collectors.toList()); //가짜 멀티파트 이미지 5장 생성

        NoticeCreateRequestDto requestDto = new NoticeCreateRequestDto("공지 제목", testUser.getId(), "공지 내용", startDate,
                endDate);
        Mockito.when(amazonS3UploadService.upload(any(), any()))
                .thenReturn(uploadedFileDto); //amazonS3Upload 객체에 가짜 행동 주입

        //when
        NoticeCreateResponseDto responseDto = noticeService.create(requestDto, mainImageMultipartFile,
                subImageMultipartFiles);//ApplicationException 발생하지 않아야 함
        em.flush();
        em.clear(); //공지 생성시에는 헤더에 이미지가 들어있지 않으므로 flush, clear 후 다시 불러와야 함

        //then
        NoticeHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getImageFiles().size()).isEqualTo(1 + subImageMultipartFiles.size()); //메인이미지 개수 + 서브이미지 개수
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle()); //request DTO에서 전달된 값들이 저장되었는지 확인
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(header.getStartDate()).isEqualTo(requestDto.getStartDate());
        assertThat(header.getEndDate()).isEqualTo(requestDto.getEndDate());
    }
}