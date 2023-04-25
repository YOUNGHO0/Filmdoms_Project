package com.filmdoms.community.banner.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.banner.data.dto.request.BannerRequestDto;
import com.filmdoms.community.banner.data.dto.response.BannerResponseDto;
import com.filmdoms.community.banner.data.entity.Banner;
import com.filmdoms.community.banner.repository.BannerRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {

    private final AccountRepository accountRepository;
    private final BannerRepository bannerRepository;
    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public List<BannerResponseDto> getMainPageBanner() {
        return bannerRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(BannerResponseDto::from)
                .toList();
    }

    @Transactional
    public BannerResponseDto create(AccountDto adminAccount, BannerRequestDto requestDto) {

        log.info("이미지 호출");
        File file = fileRepository.findById(requestDto.getFileId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        log.info("배너 생성");
        Banner banner = Banner.builder()
                .author(accountRepository.getReferenceById(adminAccount.getId()))
                .title(requestDto.getTitle())
                .file(file)
                .build();
        log.info("배너 저장");
        Banner savedBanner = bannerRepository.save(banner);
        log.info("반환 타입으로 변환");
        return BannerResponseDto.from(savedBanner);
    }

    @Transactional
    public BannerResponseDto update(Long bannerId, BannerRequestDto requestDto) {

        log.info("배너 호출");
        Banner banner = bannerRepository.findByIdWithFile(bannerId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        File file = banner.getFile();
        if (!Objects.equals(file.getId(), requestDto.getFileId())) {
            log.info("이미지 호출");
            file = fileRepository.findById(requestDto.getFileId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        }
        log.info("배너 업데이트");
        banner.update(requestDto.getTitle(), file);
        log.info("반환 타입으로 변환");
        return BannerResponseDto.from(banner);
    }

    @Transactional
    public void delete(Long bannerId) {
        log.info("배너 호출");
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND));
        log.info("배너 삭제");
        bannerRepository.delete(banner);
    }
}
