package com.filmdoms.community.vote.controller;

import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.config.TestSecurityConfig;
import com.filmdoms.community.vote.data.dto.VoteResponseDto;
import com.filmdoms.community.vote.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoteController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@DisplayName("컨트롤러 - 추천 기능")
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VoteService voteService;

    @Test
//    @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithMockUser
    @DisplayName("게시글을 추천 요청시, 정상적인 요청이라면, 게시글의 총 추천 수와 나의 추천 여부를 반환한다.")
    void givenProperRequest_whenVotingArticle_thenReturnsVoteResult() throws Exception {

        VoteResponseDto responseDto = VoteResponseDto.builder()
                .isVoted(true)
                .voteCount(1)
                .build();
        // Given
        given(voteService.toggleVote(any(), any())).willReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/article/1/vote"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                .andExpect(jsonPath("$.result.isVoted").exists())
                .andExpect(jsonPath("$.result.voteCount").exists());
    }

    @Test
//    @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @WithMockUser
    @DisplayName("게시글을 추천 요청시, 존재하지 않는 게시글이라면, 에러를 반환한다.")
    void givenInvalidPostId_whenVotingArticle_thenReturnsUriNotFoundErrorCode() throws Exception {

        // Given
        doThrow(new ApplicationException(ErrorCode.URI_NOT_FOUND))
                .when(voteService).toggleVote(any(), any());

        // When & Then
        mockMvc.perform(post("/api/v1/article/1/vote"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.resultCode == 'URI_NOT_FOUND')]").exists())
                .andExpect(jsonPath("$[?(@.result == null)]").exists());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("게시글을 추천 요청시, 미인증 유저라면, 인증 에러를 반환한다.")
    void givenUnauthenticatedUser_whenVotingArticle_thenReturnsUnauthenticatedErrorCode() throws Exception {

        // Given

        // When & Then
        mockMvc.perform(post("/api/v1/article/1/vote"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.result == null)]").exists())
                .andExpect(jsonPath("$[?(@.resultCode == 'AUTHENTICATION_ERROR')]").exists());
    }

}