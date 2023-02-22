package com.filmdoms.community.post.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.board.post.controller.PostController;
import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.dto.PostBriefDto;
import com.filmdoms.community.board.post.service.PostService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = PostController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("컨트롤러 - 게시글 서비스")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;

    @Test
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
                PostBriefDto.builder().title("title1").postCategory(PostCategory.FREE).postCommentsCount(5).build(),
                PostBriefDto.builder().title("title2").postCategory(PostCategory.SHARE).postCommentsCount(10).build(),
                PostBriefDto.builder().title("title3").postCategory(PostCategory.REVIEW).postCommentsCount(15).build(),
                PostBriefDto.builder().title("title4").postCategory(PostCategory.FREE).postCommentsCount(20).build()
        );
    }

}