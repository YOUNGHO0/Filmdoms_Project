package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
                               PostCreateRequestDto requestDto) {

        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        PostHeader header = postHeaderRepository.save(PostHeader.builder()
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .content(content)
                .mainImage(imageFileRepository.getReferenceById(requestDto.getMainImageId()))
                .build());

        imageFileService.setImageContent(requestDto.getContentImageId(), content);

        return PostBriefDto.from(header);
    }

    @Transactional
    public PostBriefDto update(AccountDto accountDto, Long postHeaderId, PostUpdateRequestDto requestDto) {
        log.info("게시글 호출");
        PostHeader header = postHeaderRepository.findByIdWithAuthorContentImage(postHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        log.info("작성자 확인");
        if (!AccountDto.from(header.getAuthor()).equals(accountDto)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, "게시글의 작성자와 일치하지 않습니다.");
        }
        log.info("게시글 업데이트");
        PostHeader updatedHeader = updateHeader(header, requestDto);
        log.info("게시글 저장");
        PostHeader savedHeader = postHeaderRepository.save(updatedHeader);
        log.info("게시글 반환 타입으로 변환");
        return PostBriefDto.from(savedHeader);
    }

    private PostHeader updateHeader(PostHeader header, PostUpdateRequestDto requestDto) {
        BoardContent content = header.getBoardContent();
        Set<ImageFile> originalImageFiles = content.getImageFiles();
        List<ImageFile> updatingImageFiles = imageFileRepository.findAllById(requestDto.getContentImageId());

        log.info("카테고리 수정");
        header.updateCategory(requestDto.getCategory());
        log.info("메인 이미지 수정");
        header.updateMainImage(imageFileRepository.getReferenceById(requestDto.getMainImageId()));
        log.info("제목 수정");
        header.updateTitle(requestDto.getTitle());
        log.info("본문 수정");
        content.updateContent(requestDto.getContent());
        log.info("없는 이미지 삭제");
        originalImageFiles.stream()
                .filter(imageFile -> !updatingImageFiles.contains(imageFile))
                .collect(Collectors.toSet()).stream() // 새로운 컬렉션으로 만들지 않으면 ConcurrentModificationException 발생
                .peek(imageFile -> log.info("삭제할 이미지 ID: {}", imageFile.getId()))
                .forEach(originalImageFiles::remove);
        log.info("새로 생긴 이미지 추가");
        updatingImageFiles.stream()
                .filter(imageFile -> !originalImageFiles.contains(imageFile))
                .peek(imageFile -> log.info("추가할 이미지 ID: {}", imageFile.getId()))
                .forEach(imageFile -> imageFile.updateBoardContent(content));
        return header;
    }

    public void delete(AccountDto accountDto, Long postHeaderId) {
        PostHeader header = postHeaderRepository.findByIdWithAuthor(postHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND));
        log.info("작성자 확인");
        if (!AccountDto.from(header.getAuthor()).equals(accountDto)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, "게시글의 작성자와 일치하지 않습니다.");
        }
        log.info("게시글 삭제");
        postHeaderRepository.delete(header);
    }
}
