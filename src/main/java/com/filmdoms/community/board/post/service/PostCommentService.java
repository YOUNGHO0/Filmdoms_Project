package com.filmdoms.community.board.post.service;

import com.filmdoms.community.account.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    // TODO: 댓글 작성 기능 구현 후 삭제
    public void testComment(Long postId) {
        postCommentRepository.save(
                PostComment.builder()
                        .author(accountRepository.getReferenceById(1L))
                        .header(postHeaderRepository.getReferenceById(postId))
                        .content("댓글 내용")
                        .build()
        );
    }
}
