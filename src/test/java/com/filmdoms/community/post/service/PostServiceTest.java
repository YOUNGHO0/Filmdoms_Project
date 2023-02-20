package com.filmdoms.community.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.filmdoms.community.account.config.SecurityConfig;
import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.post.data.constants.PostCategory;
import com.filmdoms.community.post.data.dto.PostBriefDto;
import com.filmdoms.community.post.data.entity.Post;
import com.filmdoms.community.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
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
    PostRepository postRepository;

    @Test
    @DisplayName("최근 게시글 조회를 요청하면, 최근 게시글을 4개 반환한다.")
    void givenNothing_whenSearchingRecentPosts_thenReturnsRecentPosts() {
        // Given
        given(postRepository.findFirst4ByOrderByDateCreated()).willReturn(getMockPosts());

        // When
        List<PostBriefDto> postBriefDtos = postService.getMainPagePosts();

        // Then
        assertThat(postBriefDtos).isNotEmpty();
        assertThat(postBriefDtos.size()).isEqualTo(4);
    }


    public List<Post> getMockPosts() {
        Account testAccount = Account.of(1L, "tester", "testpw", AccountRole.USER);
        return List.of(
                Post.builder()
                        .account(testAccount)
                        .postCategory(PostCategory.FREE)
                        .title("test post1")
                        .content("This is a test post.")
                        .view(0)
                        .build(),
                Post.builder()
                        .account(testAccount)
                        .postCategory(PostCategory.FREE)
                        .title("test post2")
                        .content("This is a test post.")
                        .view(0)
                        .build(),
                Post.builder()
                        .account(testAccount)
                        .postCategory(PostCategory.FREE)
                        .title("test post3")
                        .content("This is a test post.")
                        .view(0)
                        .build(),
                Post.builder()
                        .account(testAccount)
                        .postCategory(PostCategory.FREE)
                        .title("test post4")
                        .content("This is a test post.")
                        .view(0)
                        .build()
        );
    }
}