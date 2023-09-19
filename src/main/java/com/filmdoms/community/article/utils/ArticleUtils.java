package com.filmdoms.community.article.utils;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.dto.request.update.ParentUpdateRequestDto;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.exception.ApplicationException;
import com.filmdoms.community.exception.ErrorCode;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ArticleUtils {

    public static List<String> parseImage(String content) {
        String patternString = "<img src=\\\"(https?:\\/\\/[a-z./0-9-]*)\\\">";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        //  https://api.filmdoms.studio/image/940f03e2-27dc-4418-80d6-0cbd5ad0abb7.jpg 로 이미지를 뽑아 리턴
        return matcher.results().map(matchResult -> matchResult.group(1)).collect(Collectors.toList());
    }

    public static void checkTag(ParentUpdateRequestDto requestDto, Category category) {
        requestDto.getTag().verifyCategory(category);
    }

    public static void checkCategory(Article article, Category category) {
        if (article.getCategory() != category) {
            throw new ApplicationException(ErrorCode.INVALID_ARTICLE_ID);
        }
    }
    public static void checkPermission(Article article, AccountDto accountDto) {

        if (accountDto.getAccountRole() == AccountRole.ADMIN)
            return;
        if (!Objects.equals(article.getAuthor().getId(), accountDto.getId())) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }
    }
}
