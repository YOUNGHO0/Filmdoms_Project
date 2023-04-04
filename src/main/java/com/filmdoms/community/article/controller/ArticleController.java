package com.filmdoms.community.article.controller;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.dto.ArticleControllerToServiceDto;
import com.filmdoms.community.article.data.dto.ArticleRequestDto;
import com.filmdoms.community.article.data.dto.ParentRequestDto;
import com.filmdoms.community.article.data.dto.filmuniverse.FilmUniverseControllerToServiceDto;
import com.filmdoms.community.article.data.dto.filmuniverse.FilmUniverseRequestDto;
import com.filmdoms.community.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping ("/api/v1/article/{category}/{tag}")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AccountRepository accountRepository;


    @PostMapping
    public Response createArticle(@RequestBody ParentRequestDto parentRequestDto, @PathVariable Category category , @PathVariable Tag tag, @AuthenticationPrincipal AccountDto accountDto)
    {

        if(parentRequestDto instanceof ArticleRequestDto)
        {
            ArticleRequestDto requestArticleDto  = (ArticleRequestDto)parentRequestDto;
            Account userAccount = accountRepository.findByUsername(accountDto.getUsername()).orElseThrow(
                    ()->(new ApplicationException(ErrorCode.USER_NOT_FOUND,"사용자가 존재하지 않습니다")));
            ArticleControllerToServiceDto articleDto = ArticleControllerToServiceDto.from(requestArticleDto, category, tag, userAccount);
            articleService.createDefaultArticle(articleDto);
        }

        if(parentRequestDto instanceof FilmUniverseRequestDto)
        {
            FilmUniverseRequestDto requestNoticeDto =(FilmUniverseRequestDto) parentRequestDto;
            Account userAccount = accountRepository.findByUsername(accountDto.getUsername()).orElseThrow(
                    ()-> (new ApplicationException(ErrorCode.USER_NOT_FOUND,"사용자가 존재하지 않습니다")));
            FilmUniverseControllerToServiceDto noticeDto = FilmUniverseControllerToServiceDto.from(requestNoticeDto,category,tag,userAccount);
            articleService.createFilmUniverseArticle(noticeDto);
        }



        return Response.success();


    }

}
