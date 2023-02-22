package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.notice.data.dto.request.NoticeCreateRequestDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeCreateResponseDto;
import com.filmdoms.community.board.notice.data.dto.response.NoticeMainPageDto;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.board.notice.repository.NoticeHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeHeaderRepository noticeHeaderRepository;
    private final AccountRepository accountRepository;
    private final ImageFileRepository imageFileRepository; //테스트 메서드에 필요 (나중에 삭제)
    private final ImageFileService imageFileService;

    public List<NoticeMainPageDto> getMainPageDtos() {
        return noticeHeaderRepository.findTop4ByOrderByDateCreatedDesc()
                .stream()
                .map(NoticeMainPageDto::new)
                .collect(Collectors.toList());
    }

    //임시 데이터 생성 메서드 -> 나중에 삭제할 것
    public void initData() throws InterruptedException {
        Account author = Account.of("noticeUser", "1234", AccountRole.USER);
        accountRepository.save(author);

        //임시 데이터 생성에 필요한 테스트 이미지 url
        String testImageUrl = "https://filmdomsimage.s3.ap-northeast-2.amazonaws.com/124a71f5-0949-4eb5-b6fc-dacb6a67a010-123123.png";

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
            imageFileRepository.save(new ImageFile(UUID.randomUUID().toString(), "file" + i + ".png", testImageUrl, header));

            Thread.sleep(10);
        }
    }

    //추후 수정 필수 (현재 메인 이미지 파일은 noticeHeader.mainImage와 일대일 매핑 상태, noticeHeader.imageFiles와 연관 관계가 맺어지지 않음)
    public NoticeCreateResponseDto create(NoticeCreateRequestDto requestDto, MultipartFile mainImageMultipartFile, List<MultipartFile> subImageMultipartFiles) throws IOException {
        //인증 로직 필요

        Account author = accountRepository.findById(requestDto.getAccountId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        BoardContent boardContent = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        NoticeHeader header = NoticeHeader.builder()
                .title(requestDto.getTitle())
                .author(author)
                .boardContent(boardContent)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .build();

        NoticeHeader savedHeader = noticeHeaderRepository.save(header);

        imageFileService.saveImage(mainImageMultipartFile, header)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_MAIN_IMAGE_ERROR));
        imageFileService.saveImages(subImageMultipartFiles, header);

        return new NoticeCreateResponseDto(savedHeader);
    }
}
