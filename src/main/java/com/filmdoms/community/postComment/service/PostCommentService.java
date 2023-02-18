package com.filmdoms.community.postComment.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.postComment.data.entity.PostComment;
import com.filmdoms.community.postComment.repository.PostCommentRepository;
import com.filmdoms.community.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    // TODO: 댓글 작성 기능 구현 후 삭제
    public void testComment(Long postId) {
        Account testAccount = Account.of(1L, "tester", "testpw", AccountRole.USER);
        postCommentRepository.save(
                PostComment.builder()
                        .account(testAccount)
                        .post(postRepository.getReferenceById(postId))
                        .content("content")
                        .build()
        );
    }
}
