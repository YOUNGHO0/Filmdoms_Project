package com.filmdoms.community.board.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.data.dto.request.PostUpdateRequestDto;
import com.filmdoms.community.board.post.service.PostService;
import com.filmdoms.community.config.TestSecurityConfig;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
@Import(TestSecurityConfig.class)
@DisplayName("컨트롤러 - 게시글 서비스")
class PostControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;

    @Nested
    @DisplayName("게시글 조회 요청 테스트")
    class aboutPostRead {

        @Test
        @WithAnonymousUser
        @DisplayName("메인 페이지에서 게시글 조회시, 최근 게시글 4개를 반환한다.")
        void givenNothing_whenViewingPostsFromMainPage_thenReturnsFourRecentPosts() throws Exception {

            // Given
            given(postService.getMainPagePosts()).willReturn(getMockPostBriefDtos());

            // When & Then
            mockMvc.perform(get("/api/v1/post/main-page"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                    .andExpect(jsonPath("$[?(@.result.length() == 4)]").exists())
                    .andExpect(jsonPath("$[?(@.result.length() == 3)]").doesNotExist());
        }

        public List<PostBriefDto> getMockPostBriefDtos() {
            return List.of(
                    PostBriefDto.builder().title("title1").postCategory(PostCategory.FREE).commentCount(5).build(),
                    PostBriefDto.builder().title("title2").postCategory(PostCategory.SHARE).commentCount(10).build(),
                    PostBriefDto.builder().title("title3").postCategory(PostCategory.REVIEW).commentCount(15).build(),
                    PostBriefDto.builder().title("title4").postCategory(PostCategory.FREE).commentCount(20).build()
            );
        }
    }

    @Nested
    @DisplayName("게시글 생성 요청 테스트")
    class aboutPostCreate {

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 생성 요청시, 정상적인 요청이라면, 생성된 게시글 ID를 반환한다.")
        void givenCreatingPostRequest_whenCreatingPost_thenReturnsCreatedPostId() throws Exception {
            // Given
            PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L, 2L))
                    .build();
            PostBriefDto dto = PostBriefDto.builder().id(1L).build();
            given(postService.create(any(), any())).willReturn(dto);

            // When & Then
            mockMvc.perform(post("/api/v1/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                    .andExpect(jsonPath("$..result[?(@..id)]").exists());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("게시글 생성 요청시, 로그인 하지 않으면, 인증 에러를 반환한다.")
        void givenUnauthenticatedUser_whenCreatingPost_thenReturnsUserNotFoundErrorCode() throws Exception {
            // Given
            PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("title")
                    .content("content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L, 2L))
                    .build();

            // When & Then
            mockMvc.perform(post("/api/v1/post")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.result == null)]").exists())
                    .andExpect(jsonPath("$[?(@.resultCode == 'AUTHENTICATION_ERROR')]").exists());
            then(postService).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("게시글 수정 요청 테스트")
    class aboutPostUpdate {

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 수정 요청시, 정상적인 요청이라면, 수정된 게시글 ID를 반환한다.")
        void givenUpdatingPostRequest_whenUpdatingPost_thenReturnsUpdatedPostId() throws Exception {
            // Given
            PostUpdateRequestDto requestDto = getMockUpdateRequestDto();
            PostBriefDto briefDto = PostBriefDto.builder().id(1L).build();
            given(postService.update(any(), any(), any())).willReturn(briefDto);

            // When & Then
            mockMvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                    .andExpect(jsonPath("$..result[?(@..id)]").exists());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("게시글 수정 요청시, 로그인 하지 않으면, 인증 에러를 반환한다.")
        void givenUnauthenticatedUser_whenUpdatingPost_thenReturnsUserNotFoundErrorCode() throws Exception {
            // Given
            PostUpdateRequestDto requestDto = getMockUpdateRequestDto();

            // When & Then
            mockMvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'AUTHENTICATION_ERROR')]").exists())
                    .andExpect(jsonPath("$[?(@.result == null)]").exists());
            then(postService).shouldHaveNoInteractions();
        }

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 수정 요청시, 생성자와 다른 유저라면, 인증 에러를 반환한다.")
        void givenDifferentUser_whenUpdatingPost_thenReturnsInvalidPermissionErrorCode() throws Exception {
            // Given
            PostUpdateRequestDto requestDto = getMockUpdateRequestDto();
            doThrow(new ApplicationException(ErrorCode.INVALID_PERMISSION))
                    .when(postService).update(any(), any(), any());

            // When & Then
            mockMvc.perform(put("/api/v1/post/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'INVALID_PERMISSION')]").exists())
                    .andExpect(jsonPath("$[?(@.result == null)]").exists());
        }

        private PostUpdateRequestDto getMockUpdateRequestDto() {
            return PostUpdateRequestDto.builder()
                    .category(PostCategory.FREE)
                    .title("changed title")
                    .content("changed content")
                    .mainImageId(1L)
                    .contentImageId(Set.of(1L, 2L))
                    .build();
        }
    }

    @Nested
    @DisplayName("게시글 삭제 요청 테스트")
    class aboutPostDelete {

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 삭제 요청시, 정상적인 요청이라면, 성공 코드를 반환한다.")
        void givenNothing_whenViewingPostsFromMainPage_thenReturnsFourRecentPosts() throws Exception {
            // Given
            // When & Then
            mockMvc.perform(delete("/api/v1/post/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("게시글 삭제 요청시, 로그인 안된 유저라면, 인증 에러를 반환한다.")
        void givenAnonymousUser_whenDeletingPost_thenReturnsAuthenticationErrorCode() throws Exception {
            // Given
            // When & Then
            mockMvc.perform(delete("/api/v1/post/1"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'AUTHENTICATION_ERROR')]").exists())
                    .andExpect(jsonPath("$[?(@.result == null)]").exists());
        }

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 삭제 요청시, 생성자와 다른 유저라면, 권한 에러를 반환한다.")
        void givenDifferentUser_whenDeletingPost_thenReturnsInvalidPermissionErrorCode() throws Exception {
            // Given
            doThrow(new ApplicationException(ErrorCode.INVALID_PERMISSION))
                    .when(postService).delete(any(), any());

            // When & Then
            mockMvc.perform(delete("/api/v1/post/1"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'INVALID_PERMISSION')]").exists())
                    .andExpect(jsonPath("$[?(@.result == null)]").exists());
        }

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("게시글 삭제 요청시, 존재하지 않는 게시글이라면, 에러를 반환한다.")
        void givenInvalidPostId_whenDeletingPost_thenReturnsUriNotFoundErrorCode() throws Exception {
            // Given
            doThrow(new ApplicationException(ErrorCode.URI_NOT_FOUND))
                    .when(postService).delete(any(), any());

            // When & Then
            mockMvc.perform(delete("/api/v1/post/1"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'URI_NOT_FOUND')]").exists())
                    .andExpect(jsonPath("$[?(@.result == null)]").exists());
        }
    }
}