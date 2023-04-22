//package com.filmdoms.community.comment.service;
//
//import com.filmdoms.community.account.data.dto.AccountDto;
//import com.filmdoms.community.account.data.entity.Account;
//import com.filmdoms.community.account.repository.AccountRepository;
//import com.filmdoms.community.config.annotation.DataJpaTestWithJpaAuditing;
//import com.filmdoms.community.article.data.constant.Category;
//import com.filmdoms.community.article.data.constant.Tag;
//import com.filmdoms.community.article.data.entity.Article;
//import com.filmdoms.community.article.repository.ArticleRepository;
//import com.filmdoms.community.comment.data.dto.request.CommentCreateRequestDto;
//import com.filmdoms.community.comment.data.dto.request.CommentUpdateRequestDto;
//import com.filmdoms.community.comment.data.dto.response.CommentCreateResponseDto;
//import com.filmdoms.community.comment.data.dto.response.CommentVoteResponseDto;
//import com.filmdoms.community.comment.data.entity.Comment;
//import com.filmdoms.community.comment.data.entity.CommentVote;
//import com.filmdoms.community.comment.data.entity.CommentVoteKey;
//import com.filmdoms.community.comment.repository.CommentRepository;
//import com.filmdoms.community.comment.repository.CommentVoteRepository;
//import com.filmdoms.community.testentityprovider.TestAccountProvider;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@DataJpaTestWithJpaAuditing
//@DisplayName("댓글 서비스 통합 테스트")
//class CommentServiceTest {
//
//    @SpyBean
//    CommentService commentService;
//
//    @Autowired
//    CommentRepository commentRepository;
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    ArticleRepository articleRepository;
//
//    @Autowired
//    CommentVoteRepository commentVoteRepository;
//
//    @PersistenceContext
//    EntityManager em;
//
//    @Test
//    @DisplayName("부모 댓글 생성이 정상적으로 이루어지는지 확인")
//    void createParentComment() {
//        //given
//        Account articleAuthor = TestAccountProvider.get();
//        Account commentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, commentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        CommentCreateRequestDto requestDto = new CommentCreateRequestDto(article.getId(), null, "content", false);
//
//        //when
//        CommentCreateResponseDto responseDto = commentService.createComment(requestDto, AccountDto.from(commentAuthor));
//
//        //then
//        Comment comment = commentRepository.findById(responseDto.getCommentId()).get();
//        assertThat(comment.getArticle().getId()).isEqualTo(requestDto.getArticleId());
//        assertThat(comment.getParentComment()).isNull();
//        assertThat(comment.getContent()).isEqualTo(requestDto.getContent());
//        assertThat(comment.isManagerComment()).isEqualTo(requestDto.isManagerComment());
//    }
//
//    @Test
//    @DisplayName("자식 댓글 생성이 정상적으로 이루어지는지 확인")
//    void createChildComment() {
//        //given
//        Account articleAuthor = TestAccountProvider.get();
//        Account parentCommentAuthor = TestAccountProvider.get();
//        Account childCommentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, parentCommentAuthor, childCommentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        Comment parentComment = createSampleComment(article, parentCommentAuthor, null);
//        CommentCreateRequestDto requestDto = new CommentCreateRequestDto(article.getId(), parentComment.getId(), "content", false);
//
//        //when
//        CommentCreateResponseDto responseDto = commentService.createComment(requestDto, AccountDto.from(childCommentAuthor));
//
//        //then
//        Comment comment = commentRepository.findById(responseDto.getCommentId()).get();
//        assertThat(comment.getArticle().getId()).isEqualTo(requestDto.getArticleId());
//        assertThat(comment.getParentComment().getId()).isEqualTo(requestDto.getParentCommentId());
//        assertThat(comment.getContent()).isEqualTo(requestDto.getContent());
//        assertThat(comment.isManagerComment()).isEqualTo(requestDto.isManagerComment());
//    }
//
//    @Test
//    @DisplayName("댓글 수정이 정상적으로 이루어지는지 확인")
//    void updateComment() {
//        Account articleAuthor = TestAccountProvider.get();
//        Account commentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, commentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        Comment comment = createSampleComment(article, commentAuthor, null);
//        CommentUpdateRequestDto requestDto = new CommentUpdateRequestDto("updatedContent");
//
//        //when
//        commentService.updateComment(requestDto, comment.getId(), AccountDto.from(commentAuthor));
//
//        //then
//        Comment findComment = commentRepository.findById(comment.getId()).get();
//        assertThat(findComment.getContent()).isEqualTo(requestDto.getContent());
//    }
//
//    @Test
//    @DisplayName("자식 댓글이 없는 부모 댓글 삭제가 정상적으로 이루어지는지 확인")
//    void deleteComment() {
//        Account articleAuthor = TestAccountProvider.get();
//        Account commentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, commentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        Comment comment = createSampleComment(article, commentAuthor, null);
//
//        //when
//        commentService.deleteComment(comment.getId(), AccountDto.from(commentAuthor));
//
//        //then
//        assertThat(commentRepository.findById(comment.getId()).isEmpty());
//    }
//
//    @Test
//    @DisplayName("추천 안한 댓글을 추천하면 추천수가 정상적으로 증가하는지 확인")
//    void applyVoteComment() {
//        Account articleAuthor = TestAccountProvider.get();
//        Account commentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, commentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        Comment comment = createSampleComment(article, commentAuthor, null);
//
//        //when
//        CommentVoteResponseDto responseDto = commentService.toggleCommentVote(comment.getId(), AccountDto.from(articleAuthor));
//
//        //then
//        assertThat(responseDto)
//                .hasFieldOrPropertyWithValue("isVoted", true)
//                .hasFieldOrPropertyWithValue("voteCount", 1);
//    }
//
//    @Test
//    @DisplayName("추천한 댓글을 추천하면 추천수가 정상적으로 감소하는지 확인")
//    void cancelVoteComment() {
//        Account articleAuthor = TestAccountProvider.get();
//        Account commentAuthor = TestAccountProvider.get();
//        accountRepository.saveAll(List.of(articleAuthor, commentAuthor));
//
//        Article article = createSampleArticle(articleAuthor);
//        Comment comment = createSampleComment(article, commentAuthor, null);
//        createSampleVote(articleAuthor, comment);
//
//        //when
//        CommentVoteResponseDto responseDto = commentService.toggleCommentVote(comment.getId(), AccountDto.from(articleAuthor));
//
//        //then
//        assertThat(responseDto)
//                .hasFieldOrPropertyWithValue("isVoted", false)
//                .hasFieldOrPropertyWithValue("voteCount", 0);
//    }
//
//    private Article createSampleArticle(Account author) {
//        Article article = Article.builder()
//                .title("title")
//                .category(Category.MOVIE)
//                .tag(Tag.MOVIE)
//                .content("content")
//                .author(author)
//                .build();
//        return articleRepository.save(article);
//    }
//
//    private Comment createSampleComment(Article article, Account author, Comment parentComment) {
//        Comment comment = Comment.builder()
//                .article(article)
//                .author(author)
//                .parentComment(parentComment)
//                .isManagerComment(false)
//                .content("content")
//                .build();
//        return commentRepository.save(comment);
//    }
//
//    private void createSampleVote(Account voter, Comment comment) {
//        CommentVoteKey voteKey = CommentVoteKey.builder()
//                .comment(comment)
//                .account(voter)
//                .build();
//        comment.addVote();
//        commentVoteRepository.save(new CommentVote(voteKey));
//    }
//}