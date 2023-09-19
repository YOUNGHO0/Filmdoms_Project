package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ArticleUpdateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ParentUpdateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.FilmUniverseListResponseResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.MovieListResponseResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.boardlist.RecentListResponseDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.MovieAndRecentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.trending.TopFiveArticleResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.repository.ArticleRepository;
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
import java.util.stream.Collectors;

import static com.filmdoms.community.article.utils.ArticleUtils.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService implements ArticleExecutor {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CommentService commentService;
    private final VoteService voteService;


    public ArticleCreateResponseDto createArticle(ParentCreateRequestDto requestDto, AccountDto accountDto) {
        //카테고리, 태그 확인
        Category category = requestDto.getCategory();
        Tag tag = requestDto.getTag();
        tag.verifyCategory(category);
        List<String> urlList = parseImage(requestDto.getContent());
        Account author = accountRepository.getReferenceById(accountDto.getId());
        Article article = requestDto.toEntity(author, urlList.size());

        articleRepository.save(article);

        return ArticleCreateResponseDto.from(article);
    }

    public List<MovieAndRecentMainPageResponseDto> getRecentMainPageDtoList(int limit) {

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id")); //Article의 id로 역정렬

        List<Article> articles = articleRepository.findAllReturnList(pageRequest);

        return articles.stream()
                .map(MovieAndRecentMainPageResponseDto::from)
                .toList(); //commentNum은 batch_size를 이용하여 쿼리 1번으로 구해짐
    }

    public List<? extends ParentMainPageResponseDto> getMainPageDtoList(Category category, int limit) {

        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));

            List<Article> articles = articleRepository.findByCategory(category, pageRequest); //Article의 id로 역정렬

            return articles.stream()
                    .map(MovieAndRecentMainPageResponseDto::from)
                    .toList(); //commentNum은 batch_size를 이용하여 쿼리 1번으로 구해짐
    }

    public ArticleDetailResponseDto getDetail(Category category, Long articleId, AccountDto accountDto) {

            Article article = articleRepository.findByIdWithAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            if (article.getCategory() != category) {
                throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID); //해당 카테고리에는 요청으로 온 id의 Article이 없으므로 게시물 id 관련 오류가 발생
            }

            boolean isVoted = voteService.getVoteStatusOfAccountInArticle(accountDto, article);
            article.addView();

            return ArticleDetailResponseDto.from(article, isVoted);
        }


    public Page<RecentListResponseDto> getRecentArticles(Tag tag, Pageable pageable) {

        Page<Article> articles;
        if (tag == null)
            articles = articleRepository.getAllArticles(pageable);
        else
            articles = articleRepository.getAllArticlesByTag(tag, pageable);

        Page<RecentListResponseDto> recentListResponseDtos = articles.map(RecentListResponseDto::from);
        return recentListResponseDtos;
    }

    public Page<? extends ParentBoardListResponseDto> getBoardList(Category category, Tag tag, Pageable pageable) {


        Page<Article> articlesByCategory;
        if (tag != null)
            articlesByCategory = articleRepository.findArticlesByCategoryAndTag(category, tag, pageable);
        else
            articlesByCategory = articleRepository.findArticlesByCategory(category, pageable);
        Page<MovieListResponseResponseDto> movieListDtos = articlesByCategory.map(MovieListResponseResponseDto::from);
        return movieListDtos;
        }



    public List<TopFiveArticleResponseDto> getTopFiveArticles() {
        List<Article> top5Articles = articleRepository.getTop5Articles();
        List<TopFiveArticleResponseDto> topFiveArticleResponseDtos = top5Articles.stream().map(TopFiveArticleResponseDto::from).collect(Collectors.toList());
        return topFiveArticleResponseDtos;
    }


    public Page<? extends ParentBoardListResponseDto> findArticlesByKeyword(Category category, String keyword, Pageable pageable) {
        Page<Article> articles;
        switch (category) {
            case MOVIE:
                articles = articleRepository.findArticlesByKeyword(category, keyword, pageable);
                Page<MovieListResponseResponseDto> movieListResponseDtos = articles.map(MovieListResponseResponseDto::from);
                return movieListResponseDtos;
            case FILM_UNIVERSE:
                articles = articleRepository.findArticlesByKeyword(category, keyword, pageable);
                Page<FilmUniverseListResponseResponseDto> filmUniverseDto = articles.map(FilmUniverseListResponseResponseDto::from);
                return filmUniverseDto;
        }

        return null;
    }

    public Page<? extends ParentBoardListResponseDto> findArticlesByNickname(Category category, String authorName, Pageable pageable) {
        Page<Article> articles;
        switch (category) {
            case MOVIE:
                articles = articleRepository.findArticlesByNickname(category, authorName, pageable);
                Page<MovieListResponseResponseDto> movieListResponseDtos = articles.map(MovieListResponseResponseDto::from);
                return movieListResponseDtos;
            case FILM_UNIVERSE:
                articles = articleRepository.findArticlesByNickname(category, authorName, pageable);
                Page<FilmUniverseListResponseResponseDto> filmUniverseDto = articles.map(FilmUniverseListResponseResponseDto::from);
                return filmUniverseDto;
        }

        return null;
    }


    public void deleteArticle(Category category, Long articleId, AccountDto accountDto) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
        checkCategory(article, category);
        checkPermission(article, accountDto);
        commentService.deleteArticleComments(article);
        voteService.deleteArticleVotes(article);
        articleRepository.delete(article);

    }


    public void updateArticle(Category category, Long articleId, AccountDto accountDto, ParentUpdateRequestDto requestDto) {
        //URI의 category와 JSON 내부의 category가 일치하는지 확인
        if (requestDto.getCategory() != category) {
            throw new ApplicationException(ErrorCode.CATEGORY_DOES_NOT_MATCH);
        }

            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            checkCategory(article, category);
            checkTag(requestDto, category);
            checkPermission(article, accountDto);

            ArticleUpdateRequestDto articleUpdateRequestDto = (ArticleUpdateRequestDto) requestDto;
            List<String> urlList = parseImage(requestDto.getContent());
            articleUpdateRequestDto.updateEntity(article, urlList.size());

    }



}
