package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostAccountDto;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import com.filmdoms.community.file.service.ImageFileService;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {PostService.class})
@ActiveProfiles("test")
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
    @MockBean
    ImageFileRepository imageFileRepository;

    @Nested
    @DisplayName("게시글 조회 요청 테스트")
    class aboutPostRead {
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
            then(accountRepository).shouldHaveNoInteractions();
            then(imageFileService).shouldHaveNoInteractions();
            then(imageFileRepository).shouldHaveNoInteractions();
        }

    }

    @Nested
    @DisplayName("게시글 생성 요청 테스트")
    class aboutPostCreate {
        @Test
        @DisplayName("게시글 생성 요청시, 정상적인 요청이라면, 게시글 정보를 반환한다.")
        void givenPostInfo_whenSavingPost_thenSavesPostInfo() {
            // Given
            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);
            PostCreateRequestDto mockRequestDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L, 2L))
                    .build();
            given(postHeaderRepository.save(any())).willReturn(getMockPost(mockAccount, mockRequestDto));

            // When
            PostBriefDto postBriefDto = postService.create(mockAccountDto, mockRequestDto);

            // Then
            assertThat(postBriefDto)
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle())
                    .hasFieldOrPropertyWithValue("author", PostAccountDto.from(mockAccount))
                    .hasFieldOrPropertyWithValue("postCategory", mockRequestDto.getCategory());
        }
    }

    @Nested
    @DisplayName("게시글 수정 요청 테스트")
    class aboutPostUpdate {
        @Test
        @DisplayName("게시글 수정 요청시, 정상적인 요청이라면, 게시글을 수정한다.")
        void givenPostInfo_whenUpdatingPost_thenUpdatesPostInfo() {
            // Given
            Long requestHeaderId = 1L;
            PostCategory categoryToUpdate = PostCategory.REVIEW;
            String titleToUpdate = "changed title";
            String contentToUpdate = "changed content";
            Long mainImageToUpdate = 2L;
            Set<Long> contentImageToUpdate = Set.of(2L);
            ImageFile mockImageFile = mock(ImageFile.class);

            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);
            // 원본 내용
            PostCreateRequestDto createDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L))
                    .build();
            // 바꿀 내용
            PostUpdateRequestDto mockRequestDto = PostUpdateRequestDto.builder()
                    .category(categoryToUpdate)
                    .title(titleToUpdate)
                    .content(contentToUpdate)
                    .mainImageId(mainImageToUpdate)
                    .contentImageId(contentImageToUpdate)
                    .build();

            PostHeader mockPost = getMockPost(mockAccount, createDto);
            given(postHeaderRepository.findByIdWithAuthorContentImage(requestHeaderId)).willReturn(
                    Optional.of(mockPost));
            given(imageFileRepository.findById(any())).willReturn(Optional.of(mockImageFile));

            // When
            PostBriefDto postBriefDto = postService.update(mockAccountDto, requestHeaderId, mockRequestDto);

            // Then
            assertThat(postBriefDto)
                    .hasFieldOrPropertyWithValue("title", mockRequestDto.getTitle())
                    .hasFieldOrPropertyWithValue("author", PostAccountDto.from(mockAccount))
                    .hasFieldOrPropertyWithValue("postCategory", mockRequestDto.getCategory());
        }

        @Test
        @DisplayName("게시글 수정 요청시, 요청 게시글이 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidPostId_whenUpdatingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;
            PostCategory categoryToUpdate = PostCategory.REVIEW;
            String titleToUpdate = "changed title";
            String contentToUpdate = "changed content";
            Long mainImageToUpdate = 2L;
            Set<Long> contentImageToUpdate = Set.of(2L);
            ImageFile mockImageFile = mock(ImageFile.class);

            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);
            PostUpdateRequestDto mockRequestDto = PostUpdateRequestDto.builder()
                    .category(categoryToUpdate)
                    .title(titleToUpdate)
                    .content(contentToUpdate)
                    .mainImageId(mainImageToUpdate)
                    .contentImageId(contentImageToUpdate)
                    .build();

            given(postHeaderRepository.findByIdWithAuthorContentImage(requestHeaderId)).willReturn(Optional.empty());
            given(imageFileRepository.findById(any())).willReturn(Optional.of(mockImageFile));

            // WHEN
            Throwable throwable = catchThrowable(() ->
                    postService.update(mockAccountDto, requestHeaderId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_POST_ID.getMessage());
            then(postHeaderRepository).should().findByIdWithAuthorContentImage(requestHeaderId);
            then(imageFileRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("게시글 수정 요청시, 작성자와 요청 유저가 다르다면, 예외를 발생시킨다.")
        void givenAnotherUser_whenUpdatingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;
            PostCategory categoryToUpdate = PostCategory.REVIEW;
            String titleToUpdate = "changed title";
            String contentToUpdate = "changed content";
            Long mainImageToUpdate = 2L;
            Set<Long> contentImageToUpdate = Set.of(2L);
            ImageFile mockImageFile = mock(ImageFile.class);

            Account mockOwnerAccount = getMockAccount();
            Account mockRequesterAccount = getAnotherMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockRequesterAccount);
            // 원본 내용
            PostCreateRequestDto createDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L))
                    .build();
            // 바꿀 내용
            PostUpdateRequestDto mockRequestDto = PostUpdateRequestDto.builder()
                    .category(categoryToUpdate)
                    .title(titleToUpdate)
                    .content(contentToUpdate)
                    .mainImageId(mainImageToUpdate)
                    .contentImageId(contentImageToUpdate)
                    .build();

            PostHeader mockPost = getMockPost(mockOwnerAccount, createDto);
            given(postHeaderRepository.findByIdWithAuthorContentImage(requestHeaderId)).willReturn(
                    Optional.of(mockPost));
            given(imageFileRepository.findById(any())).willReturn(Optional.of(mockImageFile));

            // WHEN
            Throwable throwable = catchThrowable(() ->
                    postService.update(mockAccountDto, requestHeaderId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_PERMISSION.getMessage() + " 게시글의 작성자와 일치하지 않습니다.");
            then(postHeaderRepository).should().findByIdWithAuthorContentImage(requestHeaderId);
            then(imageFileRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("게시글 수정 요청시, 메인 이미지가 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidMainImageId_whenUpdatingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;
            PostCategory categoryToUpdate = PostCategory.REVIEW;
            String titleToUpdate = "changed title";
            String contentToUpdate = "changed content";
            Long mainImageToUpdate = 2L;
            Set<Long> contentImageToUpdate = Set.of(2L);

            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);
            // 원본 내용
            PostCreateRequestDto createDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L))
                    .build();
            // 바꿀 내용
            PostUpdateRequestDto mockRequestDto = PostUpdateRequestDto.builder()
                    .category(categoryToUpdate)
                    .title(titleToUpdate)
                    .content(contentToUpdate)
                    .mainImageId(mainImageToUpdate)
                    .contentImageId(contentImageToUpdate)
                    .build();

            PostHeader mockPost = getMockPost(mockAccount, createDto);
            given(postHeaderRepository.findByIdWithAuthorContentImage(requestHeaderId)).willReturn(
                    Optional.of(mockPost));
            given(imageFileRepository.findById(any())).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(() ->
                    postService.update(mockAccountDto, requestHeaderId, mockRequestDto));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_FILE_ID.getMessage());
            then(postHeaderRepository).should().findByIdWithAuthorContentImage(requestHeaderId);
            then(imageFileRepository).should().findById(mockRequestDto.getMainImageId());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 요청 테스트")
    class aboutPostDelete {

        @Test
        @DisplayName("게시글 삭제 요청시, 정상적인 요청이라면, 게시글을 삭제한다.")
        void givenPostInfo_whenDeletingPost_thenDeletesPost() {
            // Given
            Long requestHeaderId = 1L;

            PostCreateRequestDto createDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L))
                    .build();
            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);
            PostHeader mockPost = getMockPost(mockAccount, createDto);

            given(postHeaderRepository.findByIdWithAuthor(requestHeaderId)).willReturn(Optional.of(mockPost));

            // When & Then
            assertDoesNotThrow(() -> postService.delete(mockAccountDto, requestHeaderId));
        }

        @Test
        @DisplayName("게시글 삭제 요청시, 요청 게시글이 존재하지 않는다면, 예외를 발생시킨다.")
        void givenInvalidPostId_whenDeletingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;
            Account mockAccount = getMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockAccount);

            given(postHeaderRepository.findByIdWithAuthor(requestHeaderId)).willReturn(Optional.empty());

            // WHEN
            Throwable throwable = catchThrowable(() ->
                    postService.delete(mockAccountDto, requestHeaderId));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_POST_ID.getMessage());
        }

        @Test
        @DisplayName("게시글 삭제 요청시, 작성자와 요청 유저가 다르다면, 예외를 발생시킨다.")
        void givenAnotherUser_whenDeletingPost_thenThrowsException() {
            // Given
            Long requestHeaderId = 1L;

            PostCreateRequestDto createDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L))
                    .build();
            Account mockOwnerAccount = getMockAccount();
            Account mockRequesterAccount = getAnotherMockAccount();
            AccountDto mockAccountDto = AccountDto.from(mockRequesterAccount);
            PostHeader mockPost = getMockPost(mockOwnerAccount, createDto);

            given(postHeaderRepository.findByIdWithAuthor(requestHeaderId)).willReturn(Optional.of(mockPost));

            // WHEN
            Throwable throwable = catchThrowable(() ->
                    postService.delete(mockAccountDto, requestHeaderId));

            // THEN
            assertThat(throwable)
                    .isInstanceOf(ApplicationException.class)
                    .hasMessage(ErrorCode.INVALID_PERMISSION.getMessage() + " 게시글의 작성자와 일치하지 않습니다.");
        }
    }


    private Account getMockAccount() {
        Account mockAccount = Account.builder()
                .password("testpw")
                .email("tester@email.com")
                .nickname("nickname")
                .role(AccountRole.USER)
                .build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 2L, Long.class);
        return mockAccount;
    }

    private Account getAnotherMockAccount() {
        Account mockAccount = Account.builder()
                .password("anotherPw")
                .email("anotherUser@email.com")
                .nickname("anotherNickname")
                .role(AccountRole.USER)
                .build();
        ReflectionTestUtils.setField(mockAccount, Account.class, "id", 3L, Long.class);
        return mockAccount;
    }

    private List<PostHeader> getMockPosts(Account author) {
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

    private PostHeader getMockPost(Account author, PostCreateRequestDto requestDto) {
        BoardContent mockContent = BoardContent.builder()
                .content(requestDto.getContent())
                .build();
        Set<ImageFile> mockImageFiles = requestDto.getContentImageId().stream()
                .map(id -> {
                    ImageFile mockImageFile = ImageFile.builder()
                            .boardContent(mockContent)
                            .build();
                    ReflectionTestUtils.setField(mockImageFile, ImageFile.class, "id", id, Long.class);
                    return mockImageFile;
                })
                .collect(Collectors.toSet());
        PostHeader mockHeader = PostHeader.builder()
                .author(author)
                .category(requestDto.getCategory())
                .title(requestDto.getTitle())
                .content(mockContent)
                .mainImage(ImageFile.builder().boardContent(mockContent).build())
                .build();
        ReflectionTestUtils.setField(mockHeader, PostHeader.class, "id", 1L, Long.class);
        ReflectionTestUtils.setField(mockContent, BoardContent.class, "imageFiles",
                new HashSet<>(mockImageFiles), Set.class);
        return mockHeader;
    }
}