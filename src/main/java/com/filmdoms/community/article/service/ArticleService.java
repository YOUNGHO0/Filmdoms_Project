package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import com.filmdoms.community.article.data.dto.FilmUniverse.FilmUniverseControllerToServiceDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.NoticeDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.CriticMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.MovieAndRecentMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.NoticeMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import com.filmdoms.community.newcomment.repository.NewCommentRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FilmUniverseRepository filmUniverseRepository;
    private final CriticRepository criticRepository;
    private final FileRepository fileRepository;
    private final NewCommentRepository newCommentRepository;
    private final ImageFileService imageFileService;

    public Response createDefaultArticle(ArticleControllerToServiceDto dto) {
        Article userArticle = Article.from(dto);
        Article savedArticle = articleRepository.save(userArticle);
        imageFileService.setImageContent(dto.getContentImageId(),savedArticle.getContent());
        return Response.success(savedArticle.getId());
    }

    public Response createFilmUniverseArticle(FilmUniverseControllerToServiceDto dto) {
        Article userArticle = Article.from((ArticleControllerToServiceDto) dto);
        articleRepository.save(userArticle);
        imageFileService.setImageContent(dto.getContentImageId(),userArticle.getContent());
        FilmUniverse notice = FilmUniverse.from(userArticle, dto.getStartDate(), dto.getEndDate());
        FilmUniverse savedNotice = filmUniverseRepository.save(notice);
        return Response.success(savedNotice.getId());


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
            List<FilmUniverse> notices = FilmUniverseRepository.findAllWithArticleAuthorMainImage(pageRequest); //category 정보 필요x, Notice의 id로 역정렬

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

    public ArticleDetailResponseDto getDetail(Category category, Long articleId) {

        if (category == Category.MOVIE || category == Category.CRITIC) { //총 3번의 쿼리가 나감
            Article article = articleRepository.findByIdWithAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));
            if (article.getCategory() != category) {
                throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID); //해당 카테고리에는 요청으로 온 id의 Article이 없으므로 게시물 id 관련 오류가 발생
            }

            List<File> images = fileRepository.findByArticleId(articleId);
            List<NewComment> comments = newCommentRepository.findByArticleIdWithAuthorProfileImage(articleId);
            return ArticleDetailResponseDto.from(article, images, comments);

        } else if (category == Category.FILM_UNIVERSE) { //총 3번의 쿼리가 나감
            Notice notice = noticeRepository.findByArticleIdWithArticleAuthorProfileImageContent(articleId).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_ARTICLE_ID));

            List<File> images = fileRepository.findByArticleId(articleId);
            List<NewComment> comments = newCommentRepository.findByArticleIdWithAuthorProfileImage(articleId);
            return NoticeDetailResponseDto.from(notice, images, comments);
        }

        throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
