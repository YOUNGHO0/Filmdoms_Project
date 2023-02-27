package com.filmdoms.community.board.banner.service;

import com.filmdoms.community.board.banner.data.dto.BannerDto;
import com.filmdoms.community.board.banner.data.entity.BannerHeader;
import com.filmdoms.community.board.banner.repository.BannerRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BannerService {

    @Value("${domain}")
    private String domain;
    private final BannerRepository bannerRepository;
    private final ImageFileRepository imageFileRepository;

    @Transactional(readOnly = true)
    public List<BannerDto> getMainPageBanner() {
        return bannerRepository.findAllByOrderByIdDesc()
                .stream()
                .map(head -> BannerDto.from(head, domain))
                .collect(Collectors.toUnmodifiableList());
    }

    // TODO: 테스트용 로직. 실제 저장 로직 구현시 삭제 해야함.
    public void setData(String title, MultipartFile multipartFile) {

        BoardContent content = BoardContent.builder().build();

        ImageFile bannerImage = ImageFile.builder()
                .uuidFileName("3554e88f-d683-4f18-b3f4-33fbf6905792.webp")
                .originalFileName("popcorn-movie-party-entertainment.webp")
                .boardContent(content)
                .build();

        String bannerTitle = title == null ? "임시 타이틀입니다." : title;
        BannerHeader bannerHeader = BannerHeader.builder()
                .title(bannerTitle)
                .mainImage(bannerImage)
                .boardContent(content)
                .build();
        bannerRepository.save(bannerHeader);
    }
}
