package com.filmdoms.community.board.notice.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    @Value("${domain}")
    private String domain;
    private final NoticeHeaderRepository noticeHeaderRepository;
    private final AccountRepository accountRepository;
    private final ImageFileRepository imageFileRepository; //테스트 메서드에 필요 (나중에 삭제)
    private final ImageFileService imageFileService;

    public List<NoticeMainPageDto> getMainPageDtos() {
        return noticeHeaderRepository.findTop4ByOrderByDateCreatedDesc()
                .stream()
                .map(head -> new NoticeMainPageDto(head, domain))
                .collect(Collectors.toList());
    }

    //임시 데이터 생성 메서드 -> 나중에 삭제할 것
    public void initData() throws InterruptedException {
        Account author = Account.of("noticeUser", "1234", AccountRole.USER);
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
                    .boardHeadCore(header)
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

        BoardContent boardContent = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        NoticeHeader header = NoticeHeader.builder()
                .title(requestDto.getTitle())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .boardContent(boardContent)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .build();

        NoticeHeader savedHeader = noticeHeaderRepository.save(header);

        imageFileService.setImageHeader(requestDto.getMainImageId(), header);
        imageFileService.setImageHeader(requestDto.getSubImageIds(), header);

        return new NoticeCreateResponseDto(savedHeader);
    }
}
