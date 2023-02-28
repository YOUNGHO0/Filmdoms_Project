package com.filmdoms.community.board.banner.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmdoms.community.board.banner.data.dto.BannerDto;
import com.filmdoms.community.board.banner.data.dto.request.BannerInfoRequestDto;
import com.filmdoms.community.board.banner.service.BannerService;
import com.filmdoms.community.config.TestSecurityConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BannerController.class)
@Import(TestSecurityConfig.class)
@DisplayName("컨트롤러 - 배너 서비스")
class BannerHeaderControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BannerService bannerService;


    @Nested
    @DisplayName("배너 조회 요청 테스트")
    class aboutBannerRead {
        @Test
        @DisplayName("메인 페이지에서 배너 조회시, 시간 역순으로 배너 정보를 응답으로 반환한다.")
        void givenNothing_whenViewingBannersFromMainPage_thenReturnsRecentBanners() throws Exception {

            // Given
            given(bannerService.getMainPageBanner()).willReturn(getMockBannerDtos());

            // When & Then
            mockMvc.perform(get("/api/v1/banner/main-page"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                    .andExpect(jsonPath("$..result[?(@..title)]").exists())
                    .andExpect(jsonPath("$..result[?(@..imageUrl)]").exists());
        }
    }


    @Nested
    @DisplayName("배너 생성 요청 테스트")
    class aboutBannerCreate {

        @Test
        @WithUserDetails(value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("배너 생성 요청시, 정상적인 요청이라면, 생성된 배너 정보를 반환한다.")
        void givenCreatingBannerRequest_whenCreatingBanner_thenReturnsCreatedBannerInfo() throws Exception {
            // Given
            BannerInfoRequestDto requestDto = BannerInfoRequestDto.builder()
                    .mainImageId(1L)
                    .title("title")
                    .build();
            BannerDto dto = BannerDto.builder()
                    .id(1L)
                    .title("title")
                    .imageUrl("imageUrl.png").build();
            given(bannerService.create(any(), any())).willReturn(dto);

            // When & Then
            mockMvc.perform(post("/api/v1/banner")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.resultCode == 'SUCCESS')]").exists())
                    .andExpect(jsonPath("$..result[?(@..id)]").exists())
                    .andExpect(jsonPath("$..result[?(@..title)]").exists())
                    .andExpect(jsonPath("$..result[?(@..imageUrl)]").exists());
        }

        @Test
        @WithUserDetails(value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        @DisplayName("배너 생성 요청시, 관리자가 아니라면, 인가 에러를 반환한다.")
        void givenUnauthorizedUser_whenCreatingBanner_thenReturnsUnauthorizedErrorCode() throws Exception {
            // Given
            BannerInfoRequestDto requestDto = BannerInfoRequestDto.builder()
                    .mainImageId(1L)
                    .title("title")
                    .build();

            // When & Then
            mockMvc.perform(post("/api/v1/banner")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsBytes(requestDto)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[?(@.result == null)]").exists())
                    .andExpect(jsonPath("$[?(@.resultCode == 'AUTHORIZATION_ERROR')]").exists());
            then(bannerService).shouldHaveNoInteractions();
        }
    }

    public List<BannerDto> getMockBannerDtos() {
        return List.of(
                BannerDto.builder().id(3L).title("title3").imageUrl("http://this.is.mock.url3").build(),
                BannerDto.builder().id(2L).title("title2").imageUrl("http://this.is.mock.url2").build(),
                BannerDto.builder().id(1L).title("title1").imageUrl("http://this.is.mock.url1").build()
        );
    }

}