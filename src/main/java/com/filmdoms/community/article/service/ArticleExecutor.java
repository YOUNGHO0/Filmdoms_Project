package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.request.create.ParentCreateRequestDto;
import com.filmdoms.community.article.data.dto.request.update.ParentUpdateRequestDto;
import com.filmdoms.community.article.data.dto.response.boardlist.ParentBoardListResponseDto;
import com.filmdoms.community.article.data.dto.response.create.ArticleCreateResponseDto;
import com.filmdoms.community.article.data.dto.response.detail.ArticleDetailResponseDto;
import com.filmdoms.community.article.data.dto.response.mainpage.ParentMainPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ArticleExecutor {

    ArticleCreateResponseDto createArticle(ParentCreateRequestDto requestDto, AccountDto accountDto);
    List<? extends ParentMainPageResponseDto> getMainPageDtoList(Category category, int limit);
    ArticleDetailResponseDto getDetail(Category category, Long articleId, AccountDto accountDto);
    Page<? extends ParentBoardListResponseDto> getBoardList(Category category, Tag tag, Pageable pageable);
    void updateArticle(Category category, Long articleId, AccountDto accountDto, ParentUpdateRequestDto requestDto);
    void deleteArticle(Category category, Long articleId, AccountDto accountDto);


}
