package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.FilmUniverseUpdateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ParentUpdateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.FilmUniverseListResponseResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.FilmUniverseDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.FilmUniverseMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
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
public class FilmUniverseService implements ArticleExecutor {

    private final FilmUniverseRepository filmUniverseRepository;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CommentService commentService;
    private final VoteService voteService;

    @Override
    public ArticleCreateResponseDto createArticle(ParentCreateRequestDto requestDto, AccountDto accountDto) {

        Category category = requestDto.getCategory();
        Tag tag = requestDto.getTag();
        tag.verifyCategory(category);
        List<String> urlList = parseImage(requestDto.getContent());
        Account author = accountRepository.getReferenceById(accountDto.getId());
        Article article = requestDto.toEntity(author, urlList.size());

        articleRepository.save(article);
        FilmUniverseCreateRequestDto filmUniverseCreateRequestDto = (FilmUniverseCreateRequestDto) requestDto;

        if (urlList.isEmpty())
            throw new ApplicationException(ErrorCode.NO_IMAGE);

        FilmUniverse filmUniverse = filmUniverseCreateRequestDto.toEntity(article, urlList.get(0));
        filmUniverseRepository.save(filmUniverse);

        return ArticleCreateResponseDto.from(article);

    }

    @Override
    public List<? extends ParentMainPageResponseDto> getMainPageDtoList(Category category, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));

        List<FilmUniverse> filmUniverses = filmUniverseRepository.findAllWithArticleAuthor(pageRequest); //category 정보 필요x, Notice의 id로 역정렬

        return filmUniverses.stream()
                .map(FilmUniverseMainPageResponseDto::from)
                .toList();
    }

    @Override
    public ArticleDetailResponseDto getDetail(Category category, Long articleId, AccountDto accountDto) {

            FilmUniverse notice = filmUniverseRepository.findByArticleIdWithArticleAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            boolean isVoted = voteService.getVoteStatusOfAccountInArticle(accountDto, notice.getArticle());
            notice.getArticle().addView();
            return FilmUniverseDetailResponseDto.from(notice, isVoted);

    }


    @Override
    public Page<? extends ParentBoardListResponseDto> getBoardList(Category category, Tag tag, Pageable pageable) {

        Page<Article> articlesByCategory;
        if (tag != null)
            articlesByCategory = articleRepository.findArticlesByCategoryAndTag(category, tag, pageable);
        else
            articlesByCategory = articleRepository.findArticlesByCategory(category, pageable);
        Page<FilmUniverseListResponseResponseDto> filmUniverseListDtos = articlesByCategory.map(
                FilmUniverseListResponseResponseDto::from);

        return filmUniverseListDtos;
    }

    @Override
    public void updateArticle(Category category, Long articleId, AccountDto accountDto, ParentUpdateRequestDto requestDto) {

        if (requestDto.getCategory() != category) {
            throw new ApplicationException(ErrorCode.CATEGORY_DOES_NOT_MATCH);
        }

        FilmUniverse filmUniverse = filmUniverseRepository.findByArticleIdWithArticle(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            Article article = filmUniverse.getArticle();
            checkCategory(article, category);
            checkTag(requestDto, category);
            checkPermission(article, accountDto);

            FilmUniverseUpdateRequestDto filmUniverseUpdateRequestDto = (FilmUniverseUpdateRequestDto) requestDto;
            List<String> urlList = parseImage(requestDto.getContent());
            if (urlList == null)
                throw new ApplicationException(ErrorCode.NO_IMAGE);
            filmUniverseUpdateRequestDto.updateEntity(article, urlList.size());

            filmUniverseUpdateRequestDto.updateEntity(filmUniverse, urlList.get(0));
    }

    @Override
    public void deleteArticle(Category category, Long articleId, AccountDto accountDto) {

            FilmUniverse filmUniverse = filmUniverseRepository.findByArticleIdWithArticle(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            Article article = filmUniverse.getArticle();
            checkCategory(article, category);
            checkPermission(article, accountDto);
            commentService.deleteArticleComments(article);
            voteService.deleteArticleVotes(article);
            filmUniverseRepository.delete(filmUniverse);


    }
}
