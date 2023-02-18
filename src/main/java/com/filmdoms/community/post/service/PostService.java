package com.filmdoms.community.post.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.post.data.constants.PostCategory;
import com.filmdoms.community.post.data.dto.PostBriefDto;
import com.filmdoms.community.post.data.entity.Post;
import com.filmdoms.community.post.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 메서드 호출시, 최근 게시글 4개를 카테고리에 상관 없이 반환한다.
     * @return 최근 게시글 4개를 반환한다.
     */
    @Transactional(readOnly = true) // 이거 안붙여주면 댓글 개수 셀 때 지연로딩 때문에 오류가 난다.
    public List<PostBriefDto> getMainPagePosts() {

        return postRepository.findFirst4ByOrderByDateCreated()
                .stream()
                .map(PostBriefDto::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
