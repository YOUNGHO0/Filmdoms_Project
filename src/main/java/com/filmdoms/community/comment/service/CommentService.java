package com.filmdoms.community.comment.service;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.comment.data.dto.constant.CommentStatus;
import com.filmdoms.community.comment.data.dto.request.CommentCreateRequestDto;
import com.filmdoms.community.comment.data.dto.request.CommentUpdateRequestDto;
import com.filmdoms.community.comment.data.dto.response.CommentCreateResponseDto;
import com.filmdoms.community.comment.data.dto.response.CommentVoteResponseDto;
import com.filmdoms.community.comment.data.dto.response.DetailPageCommentResponseDto;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.data.entity.CommentVote;
import com.filmdoms.community.comment.data.entity.CommentVoteKey;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.comment.repository.CommentVoteRepository;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CommentVoteRepository commentVoteRepository;

    public DetailPageCommentResponseDto getDetailPageCommentList(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleIdWithAuthorProfileImage(articleId);
        return DetailPageCommentResponseDto.from(comments);
    }

    public CommentCreateResponseDto createComment(CommentCreateRequestDto requestDto, AccountDto accountDto) {
        Article article = articleRepository.findById(requestDto.getArticleId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));

        Account author = accountRepository.getReferenceById(accountDto.getId());

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_PARENT_COMMENT_ID));

            //위에서 조회한 Article 엔티티가 부모 댓글에 매핑된 Article 엔티티와 동일한지 확인
            if (!Objects.equals(parentComment.getArticle().getId(), article.getId())) {
                throw new ApplicationException(ErrorCode.INVALID_PARENT_COMMENT_ID);
            }

            //최상위 댓글인지 확인
            if (parentComment.getParentComment() != null) {
                throw new ApplicationException(ErrorCode.INVALID_PARENT_COMMENT_ID);
            }

            //부모 댓글이 INACTIVE, DELETED인 경우 댓글 생성 불가능하게 할지 결정 필요
        }

        if (requestDto.isManagerComment()) {
            checkManagerCommentPermission(accountDto, article);
        }

        Comment comment = Comment.builder()
                .article(article)
                .parentComment(parentComment)
                .author(author)
                .content(requestDto.getContent())
                .isManagerComment(requestDto.isManagerComment())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentCreateResponseDto.from(savedComment);
    }

    private void checkManagerCommentPermission(AccountDto accountDto, Article article) {
        //일단 관리자 댓글은 공지 게시판에서 공지 작성자와 ADMIN에 의해서만 생성되도록 함
        if (article.getCategory() != Category.FILM_UNIVERSE || (
                !Objects.equals(article.getAuthor().getId(), accountDto.getId())
                        && accountDto.getAccountRole() != AccountRole.ADMIN)) {
            throw new ApplicationException(ErrorCode.MANAGER_COMMENT_CANNOT_BE_CREATED);
        }
    }

    public void updateComment(CommentUpdateRequestDto requestDto, Long commentId, AccountDto accountDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_COMMENT_ID));

        //ACTIVE 상태인 댓글만 수정 가능
        checkCommentStatus(comment);

        //수정 권한 확인
        checkPermission(accountDto, comment);

        comment.update(requestDto.getContent());
    }

    public void deleteComment(Long commentId, AccountDto accountDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_COMMENT_ID));
        if (comment.getAuthor() == null)
            throw new ApplicationException(ErrorCode.DELETED_COMMENT);
        //ACTIVE 상태인 댓글만 삭제 가능
        checkCommentStatus(comment);

        //삭제 권한 확인
        checkPermission(accountDto, comment);

        //자식 댓글의 경우 별도 확인 없이 삭제
        if (comment.getParentComment() != null) {
            //DELETED 상태인 부모 댓글의 하나뿐인 자식 댓글이 삭제된 경우 삭제 처리
            // 자식 댓글 삭제시 부모 댓글이 삭제된 댓글이면 같이 삭제
            Comment parentComment = comment.getParentComment();
            if (parentComment.getAuthor() == null) {
                List<Comment> parentChildCommentList = commentRepository.findByParentComment(parentComment);
                if (parentChildCommentList.size() == 1 && parentChildCommentList.get(0) == comment) {
                    commentRepository.delete(comment);
                    commentRepository.delete(parentComment);
                } else {
                    commentRepository.delete(comment);
                }
            }
            return;
        }
        //부모 댓글의 경우
        if (commentRepository.existsByParentComment(comment)) {
            //자식 댓글이 있으면 게시글 내용은 삭제된 댓글입니다.와 작성자 Null처리
            comment.deleteContentAndAuthor();
            commentRepository.save(comment);
        } else {
            //자식 댓글이 없으면 삭제
            commentRepository.delete(comment);
        }
    }

    private void checkPermission(AccountDto accountDto, Comment comment) {
        if (!Objects.equals(comment.getAuthor().getId(), accountDto.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private void checkCommentStatus(Comment comment) {
        if (comment.getStatus() != CommentStatus.ACTIVE) {
            throw new ApplicationException(ErrorCode.COMMENT_NOT_ACTIVE);
        }
    }

    public CommentVoteResponseDto toggleCommentVote(Long commentId, AccountDto accountDto) {

        log.info("계정 호출");
        Account account = accountRepository.getReferenceById(accountDto.getId());

        log.info("댓글 호출");
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_COMMENT_ID));

        log.info("추천용 키 생성");
        CommentVoteKey voteKey = CommentVoteKey.builder()
                .account(account)
                .comment(comment)
                .build();

        log.info("추천 여부 확인");
        Optional<CommentVote> foundVote = commentVoteRepository.findByVoteKey(voteKey);
        int voteCount;
        boolean pressedResult;

        if (foundVote.isPresent()) {
            log.info("추천 취소");
            commentVoteRepository.delete(foundVote.get());
            voteCount = comment.removeVote();
            pressedResult = false;
        } else {
            log.info("추천 생성");
            commentVoteRepository.save(new CommentVote(voteKey));
            voteCount = comment.addVote();
            pressedResult = true;
        }

        log.info("추천 결과 반환");
        return CommentVoteResponseDto.builder()
                .voteCount(voteCount)
                .isVoted(pressedResult)
                .build();
    }
}
