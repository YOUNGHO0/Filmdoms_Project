package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import com.filmdoms.community.article.data.dto.notice.NoticeControllerToServiceDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Notice;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final NoticeRepository noticeRepository;

    public Response createDefaultArticle(ArticleControllerToServiceDto dto)
    {
            Article userArticle = Article.from(dto);
            Article savedArticle = articleRepository.save(userArticle);
            return Response.success(savedArticle.getId());
    }

    public Response createNoticeArticle(NoticeControllerToServiceDto dto)
    {
        Article userArticle = Article.from((ArticleControllerToServiceDto)dto);
        articleRepository.save(userArticle);
        Notice notice = Notice.from(userArticle,dto.getStartDate(),dto.getEndDate());
        Notice savedNotice = noticeRepository.save(notice);
        return Response.success(savedNotice.getId());
    }

}
