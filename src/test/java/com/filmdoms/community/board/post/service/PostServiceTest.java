package com.filmdoms.community.board.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostAccountDto;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@WebMvcTest(
        controllers = PostService.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("비즈니스 로직 - 회원 서비스")
class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    AccountRepository accountRepository;
    @MockBean
    PostHeaderRepository postHeaderRepository;
    @MockBean
    ImageFileService imageFileService;

    @Test
    @DisplayName("최근 게시글 조회를 요청하면, 최근 게시글을 4개 반환한다.")
    void givenNothing_whenSearchingRecentPosts_thenReturnsRecentPosts() {
        // Given
        given(postHeaderRepository.findFirst4ByOrderByIdDesc()).willReturn(getMockPosts(getMockAccount()));

        // When
        List<PostBriefDto> postBriefDtos = postService.getMainPagePosts();

        // Then
        assertThat(postBriefDtos).isNotEmpty();
        assertThat(postBriefDtos.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("게시글 저장을 요청하면, 게시글 정보를 반환한다.")
    void givenPostInfo_whenSavingPost_thenSavesPostInfo() {
        // Given
        Account mockAccount = getMockAccount();
        AccountDto mockAccountDto = AccountDto.from(mockAccount);
        PostCreateRequestDto mockRequestDto = PostCreateRequestDto.builder()
                .category(PostCategory.FREE)
                .title("title")
                .content("content")
                .build();
        given(postHeaderRepository.save(any())).willReturn(getMockPost(mockAccount, mockRequestDto));

        // When
        PostBriefDto postBriefDto = postService.create(mockAccountDto, mockRequestDto, null, null);

        // Then
        assertThat(postBriefDto)
                .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle())
                .hasFieldOrPropertyWithValue("author", PostAccountDto.from(mockAccount))
                .hasFieldOrPropertyWithValue("postCategory", mockRequestDto.getCategory());
    }

    public Account getMockAccount() {
        return Account.of(1L, "tester", "testpw", AccountRole.USER);
    }

    public List<PostHeader> getMockPosts(Account author) {
        return IntStream.range(1, 5)
                .mapToObj(number -> {
                    return PostHeader.builder()
                            .author(author)
                            .category(PostCategory.FREE)
                            .title("test post" + Integer.toString(number))
                            .content(BoardContent.builder().content("This is a test post.").build())
                            .build();
                })
                .toList();
    }

    public PostHeader getMockPost(Account author, PostCreateRequestDto requestDto) {
        return PostHeader.builder()
                .author(author)
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(BoardContent.builder().content(requestDto.getContent()).build())
                .build();
    }

}