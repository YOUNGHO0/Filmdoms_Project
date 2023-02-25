package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
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

    // TODO: 게시글 작성 기능 구현 후 삭제
    public void testPost(String title) {
        Account testAccount = Account.of(1L, "tester", "testpw", AccountRole.USER);
        postHeaderRepository.save(
                PostHeader.builder()
                        .author(testAccount)
                        .category(PostCategory.FREE)
                        .title(title)
                        .build()
        );
    }
}
