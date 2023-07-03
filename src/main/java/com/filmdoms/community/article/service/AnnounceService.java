package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.response.boardlist.AnnounceListResponseDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Announce;
import com.filmdoms.community.article.repository.AnnounceRepository;
import com.filmdoms.community.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnounceService {

    private final AnnounceRepository announceRepository;
    private final ArticleRepository articleRepository;

    public Response registerAnnounce(Long articleId) {

        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID);

        Optional<Announce> optionalAnnounce = announceRepository.findAnnounceByArticleId(articleId);
        if (optionalAnnounce.isPresent())
            throw new ApplicationException(ErrorCode.ALREADY_REGISTERED_ANNOUNCE);

        Article article = articleRepository.findById(articleId).get(); // 컨트롤러단에서 확인했기 때문에 없을수가 없음

        Announce announce = Announce.builder()
                .article(article)
                .build();
        announceRepository.save(announce);

        return Response.success();
    }

    public Response unregisterAnnounce(Long articleId) {

        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID);

        Optional<Announce> optionalAnnounce = announceRepository.findAnnounceByArticleId(articleId);
        if (optionalAnnounce.isEmpty())
            throw new ApplicationException(ErrorCode.ALREADY_UNREGISTERED_ANNOUNCE);

        Announce announce = announceRepository.findAnnounceByArticleId(articleId).get();
        announceRepository.delete(announce);
        return Response.success();
    }

    public Page<AnnounceListResponseDto> getAllAnnounceArticles(Pageable pageable) {
        Page<Announce> announces = announceRepository.findAllAnnounceList(pageable);
        Page<AnnounceListResponseDto> announceListResponseDtos = announces.map(AnnounceListResponseDto::from);
        return announceListResponseDtos;
    }

    public Page<AnnounceListResponseDto> getAnnounceArticlesByCategory(Category category, Pageable pageable) {

        Page<Announce> announces = announceRepository.findAnnounceListByCategory(category, pageable);
        Page<AnnounceListResponseDto> announceListResponseDtos = announces.map(AnnounceListResponseDto::from);
        return announceListResponseDtos;
    }
}
