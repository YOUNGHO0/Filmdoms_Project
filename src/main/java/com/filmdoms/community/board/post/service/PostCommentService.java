package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.post.data.entity.PostComment;
import com.filmdoms.community.board.post.repository.PostCommentRepository;
import com.filmdoms.community.board.post.repository.PostHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostHeaderRepository postHeaderRepository;
    private final PostCommentRepository postCommentRepository;

    // TODO: 댓글 작성 기능 구현 후 삭제
    public void testComment(Long postId) {
        Account testAccount = Account.of(1L, "tester", "testpw", AccountRole.USER);
        postCommentRepository.save(
                PostComment.builder()
                        .author(testAccount)
                        .header(postHeaderRepository.getReferenceById(postId))
                        .content("댓글 내용")
                        .build()
        );
    }
}
