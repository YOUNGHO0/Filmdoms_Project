package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.board.notice.repository.NoticeHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTestWithJpaAuditing
@DisplayName("공지 서비스-리포지토리 통합 테스트")
class NoticeServiceTest {

    @SpyBean
    NoticeService noticeService;

    @SpyBean
    ImageFileService imageFileService;

    @MockBean //나중에 ImageFileService에 AmazonS3UploadService 의존성 사라지면 삭제
    AmazonS3UploadService amazonS3UploadService;

    @Autowired
    NoticeHeaderRepository headerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ImageFileRepository imageFileRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 공지는_메인_이미지_없으면_예외_발생() {
        //given
        Account testUser = accountRepository.save(Account.builder().username("user1").role(AccountRole.ADMIN).build());
        AccountDto testAccountDto = AccountDto.from(testUser); //컨트롤러에서 받은 인증 객체 역할

        //공지 시작일 종료일 설정
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);

        //when
        NoticeCreateRequestDto requestDto = new NoticeCreateRequestDto("공지 제목", "공지 내용", startDate,
                endDate, null, null); //메인 이미지가 없음

        //then
        ApplicationException ex = assertThrows(ApplicationException.class, () -> {
            noticeService.create(requestDto, testAccountDto);
        });
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NO_MAIN_IMAGE_ERROR);
    }

    @Test
    public void 메인이미지_서브이미지_있는_공지_생성() {

        //given
        Account testUser = accountRepository.save(Account.builder().username("user1").role(AccountRole.ADMIN).build());
        AccountDto testAccountDto = AccountDto.from(testUser); //컨트롤러에서 받은 인증 객체 역할

        //공지 시작일 종료일 설정
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);

        //테스트 이미지 생성
        Long mainImageId = null;
        Set<Long> contentImageId = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Long createdImageId = createTestImage();
            contentImageId.add(createdImageId);
            if (i == 0) {
                mainImageId = createdImageId;
            }
        }

        NoticeCreateRequestDto requestDto = new NoticeCreateRequestDto("공지 제목", "공지 내용", startDate,
                endDate, mainImageId, contentImageId);

        //when
        NoticeCreateResponseDto responseDto = noticeService.create(requestDto,
                testAccountDto);//ApplicationException 발생하지 않아야 함
        em.flush();
        em.clear(); //공지 생성시에는 헤더에 이미지가 들어있지 않으므로 flush, clear 후 다시 불러와야 함

        //then
        NoticeHeader header = headerRepository.findById(responseDto.getPostId())
                .orElseThrow(() -> new RuntimeException("요청한 포스트 아이디가 존재하지 않음"));
        assertThat(header.getBoardContent().getImageFiles().size()).isEqualTo(contentImageId.size()); //메인이미지 개수 + 서브이미지 개수
        assertThat(header.getTitle()).isEqualTo(requestDto.getTitle()); //request DTO에서 전달된 값들이 저장되었는지 확인
        assertThat(header.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(header.getMainImage().getId()).isEqualTo(mainImageId);
        assertThat(header.getBoardContent().getContent()).isEqualTo(requestDto.getContent());
        assertThat(header.getStartDate()).isEqualTo(requestDto.getStartDate());
        assertThat(header.getEndDate()).isEqualTo(requestDto.getEndDate());
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