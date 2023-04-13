package com.filmdoms.community.newcomment.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.newcomment.data.dto.request.NewCommentCreateRequestDto;
import com.filmdoms.community.newcomment.data.dto.request.NewCommentUpdateRequestDto;
import com.filmdoms.community.newcomment.data.dto.response.NewCommentCreateResponseDto;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import com.filmdoms.community.newcomment.repository.NewCommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTestWithJpaAuditing
@DisplayName("댓글 서비스 통합 테스트")
class NewCommentServiceTest {

    @SpyBean
    NewCommentService commentService;

    @Autowired
    NewCommentRepository commentRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ArticleRepository articleRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("부모 댓글 생성이 정상적으로 이루어지는지 확인")
    void createParentComment() {
        //given
        Account articleAuthor = createSampleAccount("articleAuthor");
        Article article = createSampleArticle(articleAuthor);
        Account commentAuthor = createSampleAccount("commentAuthor");
        NewCommentCreateRequestDto requestDto = new NewCommentCreateRequestDto(article.getId(), null, "content", false);

        //when
        NewCommentCreateResponseDto responseDto = commentService.createComment(requestDto, AccountDto.from(commentAuthor));

        //then
        NewComment comment = commentRepository.findById(responseDto.getCommentId()).get();
        assertThat(comment.getArticle().getId()).isEqualTo(requestDto.getArticleId());
        assertThat(comment.getParentComment()).isNull();
        assertThat(comment.getContent()).isEqualTo(requestDto.getContent());
        assertThat(comment.isManagerComment()).isEqualTo(requestDto.isManagerComment());
    }

    @Test
    @DisplayName("자식 댓글 생성이 정상적으로 이루어지는지 확인")
    void createChildComment() {
        //given
        Account articleAuthor = createSampleAccount("articleAuthor");
        Article article = createSampleArticle(articleAuthor);
        Account parentCommentAuthor = createSampleAccount("parentCommentAuthor");
        Account childCommentAuthor = createSampleAccount("childCommentAuthor");
        NewComment parentComment = createSampleComment(article, parentCommentAuthor, null);
        NewCommentCreateRequestDto requestDto = new NewCommentCreateRequestDto(article.getId(), parentComment.getId(), "content", false);

        //when
        NewCommentCreateResponseDto responseDto = commentService.createComment(requestDto, AccountDto.from(childCommentAuthor));

        //then
        NewComment comment = commentRepository.findById(responseDto.getCommentId()).get();
        assertThat(comment.getArticle().getId()).isEqualTo(requestDto.getArticleId());
        assertThat(comment.getParentComment().getId()).isEqualTo(requestDto.getParentCommentId());
        assertThat(comment.getContent()).isEqualTo(requestDto.getContent());
        assertThat(comment.isManagerComment()).isEqualTo(requestDto.isManagerComment());
    }

    @Test
    @DisplayName("댓글 수정이 정상적으로 이루어지는지 확인")
    void updateComment() {
        Account articleAuthor = createSampleAccount("articleAuthor");
        Article article = createSampleArticle(articleAuthor);
        Account commentAuthor = createSampleAccount("commentAuthor");
        NewComment comment = createSampleComment(article, commentAuthor, null);
        NewCommentUpdateRequestDto requestDto = new NewCommentUpdateRequestDto("updatedContent");

        //when
        commentService.updateComment(requestDto, comment.getId(), AccountDto.from(commentAuthor));

        //then
        NewComment findComment = commentRepository.findById(comment.getId()).get();
        assertThat(findComment.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("자식 댓글이 없는 부모 댓글 삭제가 정상적으로 이루어지는지 확인")
    void deleteComment() {
        Account articleAuthor = createSampleAccount("articleAuthor");
        Article article = createSampleArticle(articleAuthor);
        Account commentAuthor = createSampleAccount("commentAuthor");
        NewComment comment = createSampleComment(article, commentAuthor, null);

        //when
        commentService.deleteComment(comment.getId(), AccountDto.from(commentAuthor));

        //then
        assertThat(commentRepository.findById(comment.getId()).isEmpty());
    }

    private Account createSampleAccount(String username) {
        Account account = Account.builder()
                .username(username)
                .build();
        return accountRepository.save(account);
    }

    private Article createSampleArticle(Account author) {
        Article article = Article.builder()
                .title("title")
                .category(Category.MOVIE)
                .tag(Tag.MOVIE)
                .content("content")
                .author(author)
                .build();
        return articleRepository.save(article);
    }

    private NewComment createSampleComment(Article article, Account author, NewComment parentComment) {
        NewComment comment = NewComment.builder()
                .article(article)
                .author(author)
                .parentComment(parentComment)
                .isManagerComment(false)
                .content("content")
                .build();
        return commentRepository.save(comment);
    }
}