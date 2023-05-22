package com.filmdoms.community.article.service;

import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.response.boardlist.AnnounceListResponseDto;
import com.filmdoms.community.article.data.entity.extra.Announce;
import com.filmdoms.community.article.repository.AnnounceRepository;
import com.filmdoms.community.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnounceService {

    private final AnnounceRepository announceRepository;
    private final ArticleRepository articleRepository;

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
