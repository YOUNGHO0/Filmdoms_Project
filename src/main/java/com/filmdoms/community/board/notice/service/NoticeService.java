package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.board.comment.repository.CommentRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.request.NoticeUpdateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeDetailResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeMainPageDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeUpdateResponseDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.board.notice.repository.NoticeHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    @Value("${domain}")
    private String domain;
    private final NoticeHeaderRepository noticeHeaderRepository;
    private final AccountRepository accountRepository;
    private final ImageFileRepository imageFileRepository;
    private final ImageFileService imageFileService;
    private final CommentRepository commentRepository;

    public List<NoticeMainPageDto> getMainPageDtos() {
        return noticeHeaderRepository.findTopWithMainImage(PageRequest.of(0, 4, Sort.Direction.DESC, "dateCreated"))
                .stream()
                .map(head -> new NoticeMainPageDto(head, domain))
                .collect(Collectors.toList());
    }

    //임시 데이터 생성 메서드 -> 나중에 삭제할 것
    public void initData() throws InterruptedException {
        Account author = Account.builder()
                .username("noticeUser")
                .password("1234")
                .role(AccountRole.USER)
                .build();
        accountRepository.save(author);

        for (int i = 0; i < 5; i++) {
            BoardContent content = BoardContent.builder()
                    .content("test notice content")
                    .build();

            NoticeHeader header = NoticeHeader.builder()
                    .title("notice " + i)
                    .author(author)
                    .boardContent(content)
                    .startDate(LocalDateTime.of(2023, 3, i + 1, 18, 0, 0))
                    .endDate(LocalDateTime.of(2023, 4, i + 1, 18, 0, 0))
                    .build();

            noticeHeaderRepository.save(header);
            imageFileRepository.save(ImageFile.builder()
                    .boardContent(content)
                    .originalFileName("popcorn-movie-party-entertainment.webp")
                    .uuidFileName("3554e88f-d683-4f18-b3f4-33fbf6905792.webp")
                    .build());

            Thread.sleep(10);
        }
    }

    public NoticeCreateResponseDto create(NoticeCreateRequestDto requestDto, AccountDto accountDto) {

        //나중에 validation 설정하면 삭제
        if(requestDto.getMainImageId() == null) {
            throw new ApplicationException(ErrorCode.NO_MAIN_IMAGE_ERROR);
        }

        if(requestDto.getContentImageId() == null || !requestDto.getContentImageId().contains(requestDto.getMainImageId())) {
            //메인 이미지는 전체 이미지 리스트에 포함되어야 함
            throw new ApplicationException(ErrorCode.MAIN_IMAGE_ID_NOT_IN_CONTENT_IMAGE_ID_LIST);
        }

        ImageFile mainImage = imageFileRepository.findById(requestDto.getMainImageId()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));

        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        NoticeHeader header = NoticeHeader.builder()
                .title(requestDto.getTitle())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .boardContent(content)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .mainImage(mainImage)
                .build();

        NoticeHeader savedHeader = noticeHeaderRepository.save(header);

        imageFileService.setImageContent(requestDto.getContentImageId(), content);

        return new NoticeCreateResponseDto(savedHeader);
    }

    public NoticeDetailResponseDto getDetail(Long noticeId) {
        NoticeHeader noticeHeader = noticeHeaderRepository.findByIdWithAuthorBoardContentMainImageContentImages(noticeId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        List<Comment> comments = commentRepository.findByHeaderIdWithAuthor(noticeId);
        return new NoticeDetailResponseDto(noticeHeader, comments);
    }

    public void deleteNotice(Long noticeId) {
        NoticeHeader noticeHeader = noticeHeaderRepository.findById(noticeId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID)); //deleteById를 사용하는 경우 없는 id로 요청했을 때 예외가 발생하지 않으므로 엔티티를 일단 찾고 삭제하는 로직으로 구현
        noticeHeaderRepository.delete(noticeHeader);
    }

    public NoticeUpdateResponseDto updateNotice(Long noticeId, NoticeUpdateRequestDto requestDto) {
        //권한 확인은 security config 설정으로 대체
        if(requestDto.getContentImageId() == null || !requestDto.getContentImageId().contains(requestDto.getMainImageId())) {
            //메인 이미지는 전체 이미지 리스트에 포함되어야 함
            throw new ApplicationException(ErrorCode.MAIN_IMAGE_ID_NOT_IN_CONTENT_IMAGE_ID_LIST);
        }
        NoticeHeader noticeHeader = noticeHeaderRepository.findById(noticeId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        ImageFile mainImageFile = imageFileRepository.findById(requestDto.getMainImageId()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        noticeHeader.update(requestDto.getTitle(), requestDto.getContent(), mainImageFile, requestDto.getStartDate(), requestDto.getEndDate());
        //이미지 변경
        imageFileService.updateImageContent(requestDto.getContentImageId(), noticeHeader.getBoardContent());
        return new NoticeUpdateResponseDto(noticeHeader);
    }
}
