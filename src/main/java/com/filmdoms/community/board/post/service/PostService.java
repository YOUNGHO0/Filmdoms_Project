package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.PostDetailDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostHeaderRepository postHeaderRepository;
    private final AccountRepository accountRepository;
    private final ImageFileService imageFileService;
    private final ImageFileRepository imageFileRepository;

    /**
     * 메서드 호출시, 최근 게시글 4개를 카테고리에 상관 없이 반환한다.
     *
     * @return 최근 게시글 4개를 반환한다.
     */
    @Transactional(readOnly = true) // 이거 안붙여주면 댓글 개수 셀 때 지연로딩 때문에 오류가 난다.
    public List<PostBriefDto> getMainPagePosts() {

        return postHeaderRepository.findFirst4ByOrderByIdDesc()
                .stream()
                .map(PostBriefDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public PostBriefDto create(AccountDto accountDto,
                               PostCreateRequestDto requestDto,
                               MultipartFile mainImage,
                               List<MultipartFile> subImages) {

        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        PostHeader header = postHeaderRepository.save(PostHeader.builder()
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .content(content)
                .build());

        imageFileService.saveImage(mainImage, header);
        imageFileService.saveImages(subImages, header);

        return PostBriefDto.from(header);
    }
    @Transactional
    public PostBriefDto update(AccountDto accountDto, Long postHeaderId, PostUpdateRequestDto requestDto) {
        // 게시글 호출
        PostHeader header = postHeaderRepository.findByIdWithAuthorContentImage(postHeaderId);
        // 작성자 확인
        if (!AccountDto.from(header.getAuthor()).equals(accountDto)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, "게시글의 작성자와 일치하지 않습니다.");
        }
        // 게시글 업데이트
        PostHeader updatedHeader = updateHeader(header, requestDto);
        // 게시글 저장
        PostHeader savedHeader = postHeaderRepository.save(updatedHeader);
        // 게시글 반환 타입으로 변환
        return PostBriefDto.from(savedHeader);

    }

    private PostHeader updateHeader(PostHeader header, PostUpdateRequestDto requestDto) {
        header.updateCategory(requestDto.getCategory());
        header.updateTitle(requestDto.getTitle());
        header.getBoardContent().updateContent(requestDto.getContent());
        header.getImageFiles()
                .forEach(imageFile -> imageFile.updateBoardHeadCore(null));
        requestDto.getImageFileIds().stream()
                .map(imageFileRepository::getReferenceById)
                .forEach(imageFile -> imageFile.updateBoardHeadCore(header));
        return header;
    }
}
