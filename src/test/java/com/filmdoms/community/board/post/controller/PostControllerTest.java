package com.filmdoms.community.board.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.data.dto.request.PostCreateRequestDto;
import com.filmdoms.community.board.post.service.PostService;
import com.filmdoms.community.config.TestSecurityConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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

    @Test
    @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("게시글 생성 요청시, 정상적인 요청이라면, 생성된 게시글 ID를 반환한다.")
    void givenCreatingPostRequest_whenCreatingPost_thenReturnsCreatedPostId() throws Exception {
        // Given
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .category(PostCategory.FREE)
                .title("title")
                .content("content")
                .build();
        PostBriefDto dto = PostBriefDto.builder().id(1L).build();
        given(postService.create(any(), any(), any(), any())).willReturn(dto);

        // When & Then
        mockMvc.perform(multipart("/api/v1/post/create")
                        .part(new MockPart("data", mapper.writeValueAsBytes(requestDto)))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$..result[?(@..id)]").exists());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("게시글 생성 요청시, 로그인 하지 않으면, 인증 에러를 반환한다.")
    void givenUnauthencicatedUser_whenCreatingPost_thenReturnsUserNotFoundErrorCode() throws Exception {
        // Given
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .category(PostCategory.FREE)
                .title("title")
                .content("content")
                .build();

        // When & Then
        mockMvc.perform(multipart("/api/v1/post/create")
                        .part(new MockPart("data", mapper.writeValueAsBytes(requestDto)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.result == null)]").exists())
                .andExpect(jsonPath("$[?(@.resultCode == 'AUTHENTICATION_ERROR')]").exists());
        then(postService).shouldHaveNoInteractions();
    }

}