package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.board.comment.repository.CommentRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.request.NoticeUpdateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeDetailResponseDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.board.notice.repository.NoticeHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Autowired
    ImageFileRepository imageFileRepository;

    @Autowired
    CommentRepository commentRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void 공지는_메인_이미지_없으면_예외_발생() {
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
    void 메인이미지_서브이미지_있는_공지_생성() {

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
            Long createdImageId = createTestImage().getId();
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

    @Test
    @DisplayName("공지 수정 요청 DTO에 맞게 공지가 수정된다.")
    void updateNotice() {
        //given
        //수정 전 데이터 만들기
        Account testUser = Account.builder().username("user1").role(AccountRole.ADMIN).build();
        accountRepository.save(testUser);
        BoardContent boardContent = BoardContent.builder().content("수정 전 공지 내용").build();
        LocalDateTime startDateBeforeUpdate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDateBeforeUpdate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);
        ImageFile mainImageBeforeUpdate = createTestImage();
        ImageFile subImage1BeforeUpdate = createTestImage();
        ImageFile subImage2BeforeUpdate = createTestImage();
        mainImageBeforeUpdate.updateBoardContent(boardContent);
        subImage1BeforeUpdate.updateBoardContent(boardContent);
        subImage2BeforeUpdate.updateBoardContent(boardContent);
        imageFileRepository.saveAll(List.of(mainImageBeforeUpdate, subImage1BeforeUpdate, subImage2BeforeUpdate));
        NoticeHeader noticeHeader = NoticeHeader.builder()
                .author(testUser)
                .title("수정 전 공지 제목")
                .boardContent(boardContent)
                .mainImage(mainImageBeforeUpdate)
                .startDate(startDateBeforeUpdate)
                .endDate(endDateBeforeUpdate)
                .build();
        headerRepository.save(noticeHeader); //수정 전 공지 저장

        //수정 후 데이터 만들기
        LocalDateTime startDateAfterUpdate = LocalDateTime.of(2023, 4, 1, 18, 0, 0);
        LocalDateTime endDateAfterUpdate = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        ImageFile mainImageAfterUpdate = createTestImage();
        ImageFile subImage1AfterUpdate = createTestImage();
        ImageFile subImage2AfterUpdate = createTestImage();
        NoticeUpdateRequestDto requestDto = new NoticeUpdateRequestDto("수정 후 공지 제목", "수정 후 공지 내용", startDateAfterUpdate, endDateAfterUpdate, mainImageAfterUpdate.getId(), Set.of(mainImageAfterUpdate.getId(), subImage1AfterUpdate.getId(), subImage2AfterUpdate.getId()));
        em.flush();
        em.clear();

        //when
        noticeService.updateNotice(noticeHeader.getId(), requestDto); //공지 수정하기
        em.flush();
        em.clear();
        NoticeHeader findHeader = headerRepository.findById(noticeHeader.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID)); //공지 불러오기
        //수정 전 공지 이미지 불러오기
        Optional<ImageFile> findMainImageBeforeUpdate = imageFileRepository.findById(mainImageBeforeUpdate.getId());
        Optional<ImageFile> findSubImage1BeforeUpdate = imageFileRepository.findById(subImage1BeforeUpdate.getId());
        Optional<ImageFile> findSubImage2BeforeUpdate = imageFileRepository.findById(subImage2BeforeUpdate.getId());


        //then
        //수정사항이 반영되었는지 확인
        assertThat(findHeader.getTitle()).isEqualTo("수정 후 공지 제목");
        assertThat(findHeader.getBoardContent().getContent()).isEqualTo("수정 후 공지 내용");
        assertThat(findHeader.getAuthor().getId()).isEqualTo(testUser.getId());
        assertThat(findHeader.getMainImage().getId()).isEqualTo(mainImageAfterUpdate.getId());
        assertThat(findHeader.getBoardContent().getImageFiles().stream().map(ImageFile::getId).toList()).containsExactlyInAnyOrder(mainImageAfterUpdate.getId(), subImage1AfterUpdate.getId(), subImage2AfterUpdate.getId());
        assertThat(findHeader.getStartDate()).isEqualTo(startDateAfterUpdate);
        assertThat(findHeader.getEndDate()).isEqualTo(endDateAfterUpdate);
        //수정 전 공지 이미지가 삭제되었는지 확인
        assertThat(findMainImageBeforeUpdate.isEmpty());
        assertThat(findSubImage1BeforeUpdate.isEmpty());
        assertThat(findSubImage2BeforeUpdate.isEmpty());
    }

    @Test
    @DisplayName("공지 삭제 시 연관된 엔티티가 전부 삭제된다.")
    void deleteNotice() {
        //given
        //삭제할 공지 데이터 생성
        Account testUser = Account.builder().username("user1").role(AccountRole.ADMIN).build();
        accountRepository.save(testUser);
        BoardContent boardContent = BoardContent.builder().content("공지 내용").build();
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);
        ImageFile mainImage = createTestImage();
        ImageFile subImage = createTestImage();
        mainImage.updateBoardContent(boardContent);
        subImage.updateBoardContent(boardContent);
        imageFileRepository.saveAll(List.of(mainImage, subImage));
        NoticeHeader noticeHeader = NoticeHeader.builder()
                .author(testUser)
                .title("공지 제목")
                .boardContent(boardContent)
                .mainImage(mainImage)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        headerRepository.save(noticeHeader); //공지 저장
        Comment comment = Comment.builder()
                .author(testUser)
                .content("댓글 내용")
                .header(noticeHeader)
                .parentComment(null).build();
        commentRepository.save(comment); //댓글 저장
        em.flush();
        em.clear();

        //when
        noticeService.deleteNotice(noticeHeader.getId()); //공지 삭제
        List<Optional> optionals = new ArrayList<>(); //삭제 엔티티를 모을 리스트
        optionals.add(headerRepository.findById(noticeHeader.getId()));
        optionals.add(Optional.ofNullable(em.find(BoardContent.class, boardContent.getId())));
        optionals.add(imageFileRepository.findById(mainImage.getId()));
        optionals.add(imageFileRepository.findById(subImage.getId()));
        optionals.add(commentRepository.findById(comment.getId()));
        Optional<Account> account = accountRepository.findById(testUser.getId());

        //then
        assertThat(optionals.stream().allMatch(Optional::isEmpty)); //연관된 엔티티 모두 삭제되었는지 확인
        assertThat(account.isPresent()); //계정은 삭제되면 안 됨
    }

    @Test
    @DisplayName("공지 조회가 누락 데이터 없이 정상적으로 이루어진다.")
    void readNotice() {
        //불러올 공지 데이터 생성
        Account testUser = Account.builder().username("user1").role(AccountRole.ADMIN).build();
        accountRepository.save(testUser);
        BoardContent boardContent = BoardContent.builder().content("공지 내용").build();
        LocalDateTime startDate = LocalDateTime.of(2023, 3, 1, 18, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 4, 1, 0, 0, 0);
        ImageFile mainImage = createTestImage();
        ImageFile subImage = createTestImage();
        mainImage.updateBoardContent(boardContent);
        subImage.updateBoardContent(boardContent);
        imageFileRepository.saveAll(List.of(mainImage, subImage));
        NoticeHeader noticeHeader = NoticeHeader.builder()
                .author(testUser)
                .title("공지 제목")
                .boardContent(boardContent)
                .mainImage(mainImage)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        headerRepository.save(noticeHeader); //공지 저장
        Comment comment1 = Comment.builder()
                .author(testUser)
                .content("댓글 내용")
                .header(noticeHeader)
                .parentComment(null).build();
        Comment comment2 = Comment.builder()
                .author(testUser)
                .content("댓글 내용")
                .header(noticeHeader)
                .parentComment(null).build();
        Comment comment3 = Comment.builder()
                .author(testUser)
                .content("댓글 내용")
                .header(noticeHeader)
                .parentComment(comment2).build();
        Comment comment4 = Comment.builder()
                .author(testUser)
                .content("댓글 내용")
                .header(noticeHeader)
                .parentComment(comment1).build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4); //댓글 저장
        em.flush();
        em.clear();

        //when
        NoticeDetailResponseDto responseDto = noticeService.getDetail(noticeHeader.getId());

        //then
        assertThat(responseDto.getTitle()).isEqualTo(noticeHeader.getTitle());
        assertThat(responseDto.getContent()).isEqualTo(noticeHeader.getBoardContent().getContent());
        assertThat(responseDto.getAuthor().getId()).isEqualTo(noticeHeader.getAuthor().getId());
        assertThat(responseDto.getView()).isEqualTo(noticeHeader.getView());
        assertThat(responseDto.getStatus()).isEqualTo(noticeHeader.getStatus());
        assertThat(responseDto.getImage().size()).isEqualTo(2); //이미지 2개
        assertThat(responseDto.getComments()).hasSize(2); //부모댓글은 2개
        assertThat(responseDto.getComments().get(0).getChildComments()).hasSize(1); //comment1의 자식 댓글 1개
        assertThat(responseDto.getComments().get(1).getChildComments()).hasSize(1); //comment2의 자식 댓글 1개
        assertThat(responseDto.getComments().get(0).getChildComments().get(0).getId()).isEqualTo(comment4.getId()); //comment1의 자식 댓글은 comment4
        assertThat(responseDto.getComments().get(1).getChildComments().get(0).getId()).isEqualTo(comment3.getId()); //comment2의 자식 댓글은 comment3
    }

    private ImageFile createTestImage() { //테스트 이미지 데이터를 생성해서 저장하고 id를 반환
        ImageFile testImageFile = ImageFile.builder()
                .originalFileName("testOriginalFileName")
                .uuidFileName("testUuidFileName")
                .build();
        ImageFile savedImageFile = imageFileRepository.save(testImageFile);
        return savedImageFile;
    }
}