package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.PostStatus;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountStatusCheck {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;


    public void checkAccountStatus(Account account) {
        switch (account.getAccountStatus()) {
            case INACTIVE:
                throw new ApplicationException(ErrorCode.INACTIVE_ACCOUNT);
            case DELETED:
                articleRepository.updateArticlesPostStatus(account, PostStatus.DELETED);
                commentRepository.updateCommentPostStatus(account, CommentStatus.DELETED);
                account.updateStatusToActive();
                accountRepository.save(account);
                break;
        }
    }

}
