package com.filmdoms.community.banner.service;

import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.banner.data.dto.BannerDto;
import com.filmdoms.community.banner.data.entity.Banner;
import com.filmdoms.community.banner.repository.BannerRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3Upload;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ImageFileRepository imageFileRepository;
    private final AmazonS3Upload amazonS3Upload;

    @Transactional(readOnly = true)
    public List<BannerDto> getMainPageBanner() {
        return bannerRepository.findAllByOrderByIdDesc()
                .stream()
                .map(BannerDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    // TODO: 테스트용 로직. 실제 저장 로직 구현시 삭제 해야함.
    public void setInitData(String title, MultipartFile multipartFile) {

        String bannerTitle = title == null ? "임시 타이틀입니다." : title;
        Banner banner = Banner.builder()
                .title(bannerTitle)
                .build();
        bannerRepository.save(banner);

        ImageFile bannerImage = ImageFile.builder()
                .fileUrl(getImageUrl(multipartFile))
                .header(banner)
                .build();
        imageFileRepository.save(bannerImage);
    }

    // TODO: 테스트용 로직. 실제 저장 로직 구현시 삭제 해야함.
    private String getImageUrl(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return "https://filmdomsimage.s3.ap-northeast-2.amazonaws.com/124a71f5-0949-4eb5-b6fc-dacb6a67a010-123123.png";
        }
        try {
            return amazonS3Upload.upload(multipartFile, UUID.randomUUID().toString(),
                    multipartFile.getOriginalFilename());
        } catch (IOException exception) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
