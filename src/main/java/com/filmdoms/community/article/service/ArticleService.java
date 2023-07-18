package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.CriticCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.FilmUniverseCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.*;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.FilmUniverseDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.CriticMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.FilmUniverseMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.MovieAndRecentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.trending.TopFiveArticleResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.comment.repository.CommentVoteRepository;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.vote.data.entity.VoteKey;
import com.filmdoms.community.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FilmUniverseRepository filmUniverseRepository;
    private final CriticRepository criticRepository;
    private final FileRepository fileRepository;
    private final VoteRepository voteRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final CommentVoteRepository commentVoteRepository;

    public ArticleCreateResponseDto createArticle(ParentCreateRequestDto requestDto, AccountDto accountDto) {
        //카테고리, 태그 확인
        Category category = requestDto.getCategory();
        Tag tag = requestDto.getTag();
        tag.verifyCategory(category);

        Account author = accountRepository.getReferenceById(accountDto.getId());
        Article article = requestDto.toEntity(author);
        articleRepository.save(article);

        if (requestDto instanceof FilmUniverseCreateRequestDto) {
            FilmUniverseCreateRequestDto filmUniverseCreateRequestDto = (FilmUniverseCreateRequestDto) requestDto;
            Long mainImageId = filmUniverseCreateRequestDto.getMainImageId();
            File mainImageFile = fileRepository.findById(mainImageId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_FILE_ID));
            FilmUniverse filmUniverse = filmUniverseCreateRequestDto.toEntity(article, mainImageFile);
            filmUniverseRepository.save(filmUniverse);
        }

        if (requestDto instanceof CriticCreateRequestDto) {
            CriticCreateRequestDto criticCreateRequestDto = (CriticCreateRequestDto) requestDto;
            Long mainImageId = criticCreateRequestDto.getMainImageId();
            File mainImageFile = fileRepository.findById(mainImageId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_FILE_ID));
            Critic critic = criticCreateRequestDto.toEntity(article, mainImageFile);
            criticRepository.save(critic);
        }

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

        if (category == Category.MOVIE) {
            List<Article> articles = articleRepository.findByCategory(category, pageRequest); //Article의 id로 역정렬

            return articles.stream()
                    .map(MovieAndRecentMainPageResponseDto::from)
                    .toList(); //commentNum은 batch_size를 이용하여 쿼리 1번으로 구해짐

        } else if (category == Category.FILM_UNIVERSE) {
            List<FilmUniverse> filmUniverses = filmUniverseRepository.findAllWithArticleAuthorMainImage(pageRequest); //category 정보 필요x, Notice의 id로 역정렬

            return filmUniverses.stream()
                    .map(FilmUniverseMainPageResponseDto::from)
                    .toList();

        } else if (category == Category.CRITIC) {
            List<Critic> critics = criticRepository.findAllWithArticleAuthorMainImageContent(pageRequest);//category 정보 필요x, Critic의 id로 역정렬

            return critics.stream()
                    .map(CriticMainPageResponseDto::from)
                    .toList();
        }
        throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    public ArticleDetailResponseDto getDetail(Category category, Long articleId, AccountDto accountDto) {

        if (category == Category.MOVIE || category == Category.CRITIC) { //총 3번의 쿼리가 나감
            Article article = articleRepository.findByIdWithAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            if (article.getCategory() != category) {
                throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID); //해당 카테고리에는 요청으로 온 id의 Article이 없으므로 게시물 id 관련 오류가 발생
            }

            boolean isVoted = getArticleVoteStatus(accountDto, article);
            article.addView();

            return ArticleDetailResponseDto.from(article, isVoted);

        } else if (category == Category.FILM_UNIVERSE) { //총 3번의 쿼리가 나감
            FilmUniverse notice = filmUniverseRepository.findByArticleIdWithArticleAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));

            boolean isVoted = getArticleVoteStatus(accountDto, notice.getArticle());
            notice.getArticle().addView();

            return FilmUniverseDetailResponseDto.from(notice, isVoted);
        }

        throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
    }

    private boolean getArticleVoteStatus(AccountDto accountDto, Article article) { //해당 Account가 Article을 추천했는지 boolean으로 반환
        if (accountDto != null) {
            VoteKey voteKey = VoteKey.builder()
                    .account(accountRepository.getReferenceById(accountDto.getId()))
                    .article(article)
                    .build();
            return voteRepository.findByVoteKey(voteKey).isPresent();
        }
        return false; //로그인하지 않은 익명 사용자의 경우 항상 false를 반환
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
        Page<Critic> critics;
        switch (category) {
            case MOVIE:
                if (tag != null)
                    articlesByCategory = articleRepository.findArticlesByCategoryAndTag(category, tag, pageable);
                else
                    articlesByCategory = articleRepository.findArticlesByCategory(category, pageable);
                Page<MovieListResponseResponseDto> movieListDtos = articlesByCategory.map(MovieListResponseResponseDto::from);
                return movieListDtos;
            case CRITIC:
                if (tag != null)
                    critics = criticRepository.getCriticsByTag(tag, pageable);
                else
                    critics = criticRepository.getCritics(pageable);
                Page<CriticListResponseResponseDto> criticDtos = critics.map(CriticListResponseResponseDto::from);
                return criticDtos;
            case FILM_UNIVERSE:
                if (tag != null)
                    articlesByCategory = articleRepository.findArticlesByCategoryAndTag(category, tag, pageable);
                else
                    articlesByCategory = articleRepository.findArticlesByCategory(category, pageable);
                Page<FilmUniverseListResponseResponseDto> filmUniverseListDtos = articlesByCategory.map(
                        FilmUniverseListResponseResponseDto::from);
                return filmUniverseListDtos;

        }

        return null;

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

        if (category == Category.MOVIE) {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            checkCategory(article, category);
            checkPermission(article, accountDto);
            deleteArticleComments(article);
            deleteArticleVotes(article);
            articleRepository.delete(article);

        } else if (category == Category.FILM_UNIVERSE) {
            FilmUniverse filmUniverse = filmUniverseRepository.findByArticleIdWithArticle(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            Article article = filmUniverse.getArticle();
            checkCategory(article, category);
            checkPermission(article, accountDto);
            deleteArticleComments(article);
            deleteArticleVotes(article);
            filmUniverseRepository.delete(filmUniverse);

        } else if (category == Category.CRITIC) {
            Critic critic = criticRepository.findByArticleIdWithArticle(articleId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            Article article = critic.getArticle();
            checkCategory(article, category);
            checkPermission(article, accountDto);
            deleteArticleComments(article);
            deleteArticleVotes(article);
            criticRepository.delete(critic);
        }
    }

    private void deleteArticleComments(Article article) {
        List<Comment> comments = commentRepository.findByArticle(article);
        //댓글의 연관 Vote 객체 삭제
        deleteCommentsVotes(comments);

        Map<Boolean, List<Comment>> isParentCommentToCommentList = comments.stream()
                .collect(Collectors.partitioningBy(comment -> comment.getParentComment() == null));
        //자식 댓글부터 삭제
        commentRepository.deleteAll(isParentCommentToCommentList.get(false));
        //부모 댓글 삭제
        commentRepository.deleteAll(isParentCommentToCommentList.get(true));
    }

    private void deleteArticleVotes(Article article) {
        voteRepository.deleteByArticle(article);
    }

    private void deleteCommentsVotes(List<Comment> comments) {
        commentVoteRepository.deleteByComments(comments);
    }

    private void checkPermission(Article article, AccountDto accountDto) {
        if (!Objects.equals(article.getAuthor().getId(), accountDto.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private void checkCategory(Article article, Category category) {
        if (article.getCategory() != category) {
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID);
        }
    }
}
