package com.filmdoms.community.banner.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.banner.data.dto.BannerDto;
import com.filmdoms.community.banner.data.entity.Banner;
import com.filmdoms.community.banner.service.BannerService;
import com.filmdoms.community.post.controller.PostController;
import com.filmdoms.community.post.data.constants.PostCategory;
import com.filmdoms.community.post.data.dto.PostBriefDto;
import com.filmdoms.community.post.service.PostService;
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
        controllers = BannerController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("컨트롤러 - 배너 서비스")
class BannerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BannerService bannerService;

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

    public List<BannerDto> getMockBannerDtos() {
        return List.of(
                BannerDto.builder().id(3L).title("title3").imageUrl("http://this.is.mock.url3").build(),
                BannerDto.builder().id(2L).title("title2").imageUrl("http://this.is.mock.url2").build(),
                BannerDto.builder().id(1L).title("title1").imageUrl("http://this.is.mock.url1").build()
        );
    }

}