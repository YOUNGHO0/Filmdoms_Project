package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.file.service.ImageFileService;
import java.util.List;
import java.util.Objects;
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

        log.info("컨텐츠 생성");
        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();
        log.info("헤더 생성");
        PostHeader header = postHeaderRepository.save(PostHeader.builder()
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .content(content)
                .mainImage(imageFileRepository.getReferenceById(requestDto.getMainImageId()))
                .build());
        log.info("이미지 매핑");
        //imageFileService.setImageContent(requestDto.getContentImageId(), content);
        log.info("게시글 저장");
        PostHeader savedHeader = postHeaderRepository.save(header);

        return PostBriefDto.from(savedHeader);
    }

    @Transactional
    public PostBriefDto update(AccountDto accountDto, Long postHeaderId, PostUpdateRequestDto requestDto) {
        log.info("게시글 호출");
        PostHeader header = postHeaderRepository.findByIdWithAuthorContentImage(postHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        log.info("작성자 확인");
        if (!Objects.equals(header.getAuthor().getId(), accountDto.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, "게시글의 작성자와 일치하지 않습니다.");
        }
        log.info("메인 이미지 호출");
        ImageFile mainImageFile = imageFileRepository.findById(requestDto.getMainImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_FILE_ID));
        log.info("게시글 업데이트");
        header.update(requestDto.getCategory(), requestDto.getTitle(), requestDto.getContent(), mainImageFile);
        log.info("이미지 매핑");
        //imageFileService.updateImageContent(requestDto.getContentImageId(), header.getBoardContent());
        log.info("게시글 반환 타입으로 변환");
        return PostBriefDto.from(header);
    }

    public void delete(AccountDto accountDto, Long postHeaderId) {
        log.info("게시글 호출");
        PostHeader header = postHeaderRepository.findByIdWithAuthor(postHeaderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        log.info("작성자 확인");
        if (!Objects.equals(header.getAuthor().getId(), accountDto.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, "게시글의 작성자와 일치하지 않습니다.");
        }
        log.info("게시글 삭제");
        postHeaderRepository.delete(header);
    }
}
