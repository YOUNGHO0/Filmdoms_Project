package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import com.filmdoms.community.article.data.dto.filmuniverse.FilmUniverseControllerToServiceDto;
import com.filmdoms.community.article.data.dto.response.ArticleMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.CriticMainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.MainPageResponseDto;
import com.filmdoms.community.article.data.dto.response.NoticeMainPageResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.NoticeRepository;
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
    private final NoticeRepository noticeRepository;
    private final CriticRepository criticRepository;

    public Response createDefaultArticle(ArticleControllerToServiceDto dto)
    {
            Article userArticle = Article.from(dto);
            Article savedArticle = articleRepository.save(userArticle);
            return Response.success(savedArticle.getId());
    }

    public Response createFilmUniverseArticle(FilmUniverseControllerToServiceDto dto)
    {
        Article userArticle = Article.from((ArticleControllerToServiceDto)dto);
        articleRepository.save(userArticle);
        imageFileService.setImageContent(dto.getContentImageId(),userArticle.getContent());
        FilmUniverse filmUniverse = FilmUniverse.from(userArticle,dto.getStartDate(),dto.getEndDate());
        FilmUniverse savedFilmUniverse = noticeRepository.save(filmUniverse);
        return Response.success(savedFilmUniverse.getId());
    }

    public List<? extends MainPageResponseDto> getMainPageDtoList(Category category, int limit) {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        if(category == Category.MOVIE) {
            List<Article> articles = articleRepository.findByCategory(category, PageRequest.of(0, limit, sortById)); //Article의 id로 정렬

            return articles.stream()
                    .map(ArticleMainPageResponseDto::from)
                    .toList(); //commentNum은 batch_size를 이용하여 쿼리 1번으로 구해짐

        } else if(category == Category.FILM_UNIVERSE) {
            List<FilmUniverse> filmUniverses = noticeRepository.findAllWithArticleAuthorMainImage(PageRequest.of(0, limit, sortById)); //category 정보 필요x, Notice의 id로 정렬

            return filmUniverses.stream()
                    .map(NoticeMainPageResponseDto::from)
                    .toList();

        } else if(category == Category.CRITIC) {
            List<Critic> critics = criticRepository.findAllWithArticleAuthorMainImageContent(PageRequest.of(0, limit, sortById));//category 정보 필요x, Critic의 id로 정렬

            return critics.stream()
                    .map(CriticMainPageResponseDto::from)
                    .toList();
        }
        throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
