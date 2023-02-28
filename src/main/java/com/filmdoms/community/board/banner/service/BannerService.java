package com.filmdoms.community.board.banner.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.banner.data.dto.BannerDto;
import com.filmdoms.community.board.banner.data.dto.request.BannerInfoRequestDto;
import com.filmdoms.community.board.banner.data.entity.BannerHeader;
import com.filmdoms.community.board.banner.repository.BannerHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {

    @Value("${domain}")
    private String domain;
    private final AccountRepository accountRepository;
    private final BannerHeaderRepository bannerHeaderRepository;
    private final ImageFileRepository imageFileRepository;
    private final ImageFileService imageFileService;

    @Transactional(readOnly = true)
    public List<BannerDto> getMainPageBanner() {
        return bannerHeaderRepository.findAllByOrderByIdDesc()
                .stream()
                .map(head -> BannerDto.from(head, domain))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public BannerDto create(AccountDto adminAccount, BannerInfoRequestDto requestDto) {

        log.info("컨텐츠 생성");
        BoardContent content = BoardContent.builder().build();
        log.info("헤더 생성");
        BannerHeader bannerHeader = BannerHeader.builder()
                .author(accountRepository.getReferenceById(adminAccount.getId()))
                .title(requestDto.getTitle())
                .mainImage(imageFileRepository.getReferenceById(requestDto.getMainImageId()))
                .content(content)
                .build();
        log.info("이미지 매핑");
        imageFileService.setImageContent(requestDto.getMainImageId(), content);
        log.info("배너 저장");
        BannerHeader savedHeader = bannerHeaderRepository.save(bannerHeader);
        log.info("반환 타입으로 변환");
        return BannerDto.from(savedHeader, domain);
    }

    @Transactional
    public BannerDto update(AccountDto adminAccount, Long bannerHeaderId, BannerInfoRequestDto requestDto) {
        log.info("배너 호출");
        BannerHeader header = bannerHeaderRepository.findById(bannerHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND));
        log.info("배너 업데이트");
        BannerHeader updatedHeader = updateHeader(header, requestDto);
        log.info("배너 저장");
        BannerHeader savedHeader = bannerHeaderRepository.save(updatedHeader);
        log.info("반환 타입으로 변환");
        return BannerDto.from(savedHeader, domain);
    }

    private BannerHeader updateHeader(BannerHeader header, BannerInfoRequestDto requestDto) {
        BoardContent content = header.getBoardContent();
        ImageFile mainImageFile = imageFileRepository.findById(requestDto.getMainImageId()).orElseThrow(
                () -> new ApplicationException(ErrorCode.NO_IMAGE_ERROR)
        );
        Set<ImageFile> originalImageFiles = content.getImageFiles();
        Set<ImageFile> updatingImageFiles = Set.of(mainImageFile);
        log.info("제목 수정");
        header.updateTitle(requestDto.getTitle());
        log.info("메인 이미지 수정");
        header.updateMainImage(mainImageFile);
        originalImageFiles.stream()
                .filter(imageFile -> !updatingImageFiles.contains(imageFile))
                .collect(Collectors.toSet()).stream()
                .peek(imageFile -> log.info("삭제할 이미지 ID: {}", imageFile.getId()))
                .forEach(originalImageFiles::remove);
        updatingImageFiles.stream()
                .filter(imageFile -> !originalImageFiles.contains(imageFile))
                .peek(imageFile -> log.info("추가할 이미지 ID: {}", imageFile.getId()))
                .forEach(imageFile -> imageFile.updateBoardContent(content));
        return header;
    }

    @Transactional
    public void delete(Long bannerHeaderId) {
        log.info("배너 호출");
        BannerHeader header = bannerHeaderRepository.findById(bannerHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND));
        log.info("배너 삭제");
        bannerHeaderRepository.delete(header);
    }
}
