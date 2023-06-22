package com.filmdoms.community.vote.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.vote.data.dto.VoteResponseDto;
import com.filmdoms.community.vote.data.entity.Vote;
import com.filmdoms.community.vote.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {VoteService.class})
@ActiveProfiles("test")
@DisplayName("비즈니스 로직 - 게시글 추천")
class VoteServiceTest {

    @Autowired
    VoteService voteService;
    @MockBean
    ArticleRepository articleRepository;
    @MockBean
    AccountRepository accountRepository;
    @MockBean
    VoteRepository voteRepository;

    @Test
    @DisplayName("추천 요청이 들어올 때, 이미 추천한 게시글이면, 추천을 삭제한 결과를 반환한다.")
    void givenAlreadyVotedArticle_whenTogglingArticleVote_thenReturnsDeletedResult() {

        long articleId = 1L;
        int previousVoteCount = 1;
        int expectedVoteCount = 0;
        AccountDto mockAccountDto = mock(AccountDto.class);
        Account mockAccount = mock(Account.class);
        Article mockArticle = mock(Article.class);
        Vote mockVote = mock(Vote.class);

        // Given
        given(accountRepository.getReferenceById(any())).willReturn(mockAccount);
        given(articleRepository.findById(any())).willReturn(Optional.of(mockArticle));
        given(voteRepository.findByVoteKey(any())).willReturn(Optional.of(mockVote));
        given(mockAccountDto.getId()).willReturn(1L);
        given(mockArticle.removeVote()).willReturn(previousVoteCount - 1);

        // When
        VoteResponseDto result = voteService.toggleVote(mockAccountDto, articleId);

        // Then
        assertThat(result.getVoteCount()).isEqualTo(expectedVoteCount);
        assertThat(result.getIsVoted()).isEqualTo(false);
    }

    @Test
    @DisplayName("추천 요청이 들어올 때, 아직 추천하지 않은 게시글이면, 추천을 추가한 결과를 반환한다.")
    void givenNotVotedArticle_whenTogglingArticleVote_thenReturnsAddedResult() {

        long articleId = 1L;
        int previousVoteCount = 1;
        int expectedVoteCount = 2;
        AccountDto mockAccountDto = mock(AccountDto.class);
        Account mockAccount = mock(Account.class);
        Article mockArticle = mock(Article.class);

        // Given
        given(accountRepository.getReferenceById(any())).willReturn(mockAccount);
        given(articleRepository.findById(any())).willReturn(Optional.of(mockArticle));
        given(voteRepository.findByVoteKey(any())).willReturn(Optional.empty());
        given(mockAccountDto.getId()).willReturn(1L);
        given(mockArticle.addVote()).willReturn(previousVoteCount + 1);

        // When
        VoteResponseDto result = voteService.toggleVote(mockAccountDto, articleId);

        // Then
        assertThat(result.getVoteCount()).isEqualTo(expectedVoteCount);
        assertThat(result.getIsVoted()).isEqualTo(true);
    }

    @Test
    @DisplayName("추천 요청이 들어올 때, 요청 게시글이 존재하지 않는다면, 예외를 발생시킨다.")
    void givenInvalidArticleId_whenTogglingArticleVote_thenThrowsException() {

        long articleId = 1L;
        AccountDto mockAccountDto = mock(AccountDto.class);
        Account mockAccount = mock(Account.class);

        // Given
        given(accountRepository.getReferenceById(any())).willReturn(mockAccount);
        given(articleRepository.findById(any())).willReturn(Optional.empty());
        given(mockAccountDto.getId()).willReturn(1L);

        // When
        Throwable throwable = catchThrowable(() ->
                voteService.toggleVote(mockAccountDto, articleId));

        // Then
        assertThat(throwable)
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorCode.INVALID_ARTICLE_ID.getMessage());
        then(accountRepository).should().getReferenceById(any());
        then(voteRepository).shouldHaveNoInteractions();
    }
}