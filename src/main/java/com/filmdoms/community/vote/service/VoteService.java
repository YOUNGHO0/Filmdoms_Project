package com.filmdoms.community.vote.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.vote.data.dto.VoteResponseDto;
import com.filmdoms.community.vote.data.entity.Vote;
import com.filmdoms.community.vote.data.entity.VoteKey;
import com.filmdoms.community.vote.repository.VoteRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public VoteResponseDto toggleVote(AccountDto accountDto, Long articleId) {

        Account account = accountRepository.getReferenceById(accountDto.getId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));

        VoteKey voteKey = VoteKey.builder()
                .account(account)
                .article(article)
                .build();

        Optional<Vote> foundVote = voteRepository.findByVoteKey(voteKey);
        int voteCount;
        boolean pressedResult;

        if (foundVote.isPresent()) {
            voteRepository.delete(foundVote.get());
            voteCount = article.removeVote();
            pressedResult = false;
        } else {
            voteRepository.save(new Vote(voteKey));
            voteCount = article.addVote();
            pressedResult = true;
        }

        return VoteResponseDto.builder()
                .voteCount(voteCount)
                .isVoted(pressedResult)
                .build();
    }
}
