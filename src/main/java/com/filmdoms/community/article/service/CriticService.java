package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.CriticUpdateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ParentUpdateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.CriticListResponseResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.CriticMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.comment.service.CommentService;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.filmdoms.community.article.utils.ArticleUtils.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CriticService implements ArticleExecutor {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CriticRepository criticRepository;
    private final VoteService voteService;
    private final CommentService commentService;


    @Override
    public ArticleCreateResponseDto createArticle(ParentCreateRequestDto requestDto, AccountDto accountDto) {
        Category category = requestDto.getCategory();
        Tag tag = requestDto.getTag();
        tag.verifyCategory(category);
        List<String> urlList = parseImage(requestDto.getContent());
        Account author = accountRepository.getReferenceById(accountDto.getId());
        Article article = requestDto.toEntity(author, urlList.size());

        articleRepository.save(article);

        if (requestDto instanceof CriticCreateRequestDto) {
            CriticCreateRequestDto criticCreateRequestDto = (CriticCreateRequestDto) requestDto;

            if (urlList.isEmpty() || urlList.size() < 3)
                throw new ApplicationException(ErrorCode.MORE_IMAGE_REQUIRED);
            Critic critic = criticCreateRequestDto.toEntity(article, urlList.get(0));
            criticRepository.save(critic);
        }

        return ArticleCreateResponseDto.from(article);
    }



    public List<? extends ParentMainPageResponseDto> getMainPageDtoList(Category category, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));
        List<Critic> critics = criticRepository.findAllWithArticleAuthorContent(pageRequest);//category 정보 필요x, Critic의 id로 역정렬

            return critics.stream()
                    .map(CriticMainPageResponseDto::from)
                    .toList();
    }

    @Override
    public ArticleDetailResponseDto getDetail(Category category, Long articleId, AccountDto accountDto) {

        Article article = articleRepository.findByIdWithAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
        if (article.getCategory() != category) {
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID); //해당 카테고리에는 요청으로 온 id의 Article이 없으므로 게시물 id 관련 오류가 발생
        }

        boolean isVoted = voteService.getVoteStatusOfAccountInArticle(accountDto, article);
        article.addView();

        return ArticleDetailResponseDto.from(article, isVoted);
    }


    @Override
    public Page<? extends ParentBoardListResponseDto> getBoardList(Category category, Tag tag, Pageable pageable) {
        Page<Critic> critics;
        if (tag != null)
            critics = criticRepository.getCriticsByTag(tag, pageable);
        else
            critics = criticRepository.getCritics(pageable);
        Page<CriticListResponseResponseDto> criticDtos = critics.map(CriticListResponseResponseDto::from);
        return criticDtos;
    }

    @Override
    public void updateArticle(Category category, Long articleId, AccountDto accountDto, ParentUpdateRequestDto requestDto) {

        Critic critic = criticRepository.findByArticleIdWithArticle(articleId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
        Article article = critic.getArticle();
        checkCategory(article, category);
        checkTag(requestDto, category);
        checkPermission(article, accountDto);
        List<String> urlList = parseImage(requestDto.getContent());
        if (urlList == null || urlList.size() < 3)
            throw new ApplicationException(ErrorCode.MORE_IMAGE_REQUIRED);
        CriticUpdateRequestDto criticUpdateRequestDto = (CriticUpdateRequestDto) requestDto;
        criticUpdateRequestDto.updateEntity(article, urlList.size());
        criticUpdateRequestDto.updateEntity(critic, urlList.get(0));

    }

    @Override
    public void deleteArticle(Category category, Long articleId, AccountDto accountDto) {
        Critic critic = criticRepository.findByArticleIdWithArticle(articleId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
        Article article = critic.getArticle();
        checkCategory(article, category);
        checkPermission(article, accountDto);
        commentService.deleteArticleComments(article);
        voteService.deleteArticleVotes(article);
        criticRepository.delete(critic);
    }
}
