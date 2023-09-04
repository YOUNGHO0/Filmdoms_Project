package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.PostStatus;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public Response deleteAccount(Long accountId) {
        Optional<Account> optionalUserAccount = accountRepository.findById(accountId);
        if (optionalUserAccount.isPresent()) {
            accountService.deleteExpiredAccount(optionalUserAccount.get());
            return Response.success();
        }
        return Response.error(ErrorCode.USER_NOT_EXIST.getMessage());
    }

    public Response suspendUser(Long accountId){

        Account account = accountRepository.findById(accountId)
                .orElseThrow(()->new ApplicationException(ErrorCode.USER_NOT_FOUND));
        articleRepository.updateArticlesPostStatus(account, PostStatus.DELETED);
        commentRepository.updateCommentPostStatus(account, CommentStatus.DELETED);
        account.updateStatusToDeleted(LocalDateTime.now());
        accountRepository.save(account);

        return Response.success();
    }

}
