package com.filmdoms.community.newcomment.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.board.data.constant.CommentStatus;
import com.filmdoms.community.newcomment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import com.filmdoms.community.newcomment.repository.NewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NewCommentService {

    private final NewCommentRepository newCommentRepository;

    public DetailPageCommentResponseDto getDetailPageCommentList(Long articleId) {
        List<NewComment> comments = newCommentRepository.findByArticleIdWithAuthorProfileImage(articleId);
        return DetailPageCommentResponseDto.from(comments);
    }
}
