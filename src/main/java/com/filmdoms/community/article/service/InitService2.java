package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.file.data.entity.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService2 {

    private final InitServiceGenerator initServiceGenerator;

    public void initData() {
        File tempFile = initServiceGenerator.fileGenerator("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png", "original_file_name21");

        Account testAccount = initServiceGenerator.accountGenerator("hello", "hellonickname", AccountRole.USER, tempFile);
        Account testAccount2 = initServiceGenerator.accountGenerator("ironman", "imironman", AccountRole.USER, tempFile);
        List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                .filter(tag -> tag.getCategory() == Category.MOVIE)
                .toList();
        //영화 게시판 생성
        // 이미지 포함 미포함, 작성자 testAccount, testAccount2 로 총 4가지 종류의 테스트 데이터 생성
        for (int i = 0; i < 20; i++) {
            // 이미지 미포함 게시글 작성자 testAccount
            for (Tag tag : movieTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.MOVIE, tag, testAccount, false, "영화게시글 " + i + "번째 태그 :" + tag + "내용");
            }
            //이미지 포함 게시글 작성자 testAccount
            for (Tag tag : movieTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀 이미지있음" + i + "번째",
                        Category.MOVIE, tag, testAccount, true, "영화게시글 " + i + "번째 태그 :" + tag + "내용에 이미지 있음");
            }
            // 이미지 미포함 게시글 작성자 testAccount2
            for (Tag tag : movieTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.MOVIE, tag, testAccount2, false, "영화게시글 " + i + "번째 태그 :" + tag + "내용");
            }
            //이미지 포함 게시글 작성자 testAccount2
            for (Tag tag : movieTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀 이미지있음" + i + "번째",
                        Category.MOVIE, tag, testAccount2, true, "영화게시글 " + i + "번째 태그 :" + tag + "내용에 이미지 있음");
            }
        }
        List<Tag> criticTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                .filter(tag -> tag.getCategory() == Category.CRITIC)
                .toList();

        //critic 게시판 생성
        for (int i = 0; i < 20; i++) {
            for (Tag tag : criticTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.CRITIC, tag, testAccount, false, "비평게시물" + i + "번째 태그 :" + tag + "내용");
            }
            for (Tag tag : criticTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀 이미지있음" + i + "번째",
                        Category.CRITIC, tag, testAccount, true, "비평게시물" + i + "번째 태그 :" + tag + "내용에 이미지 있음");
            }
            for (Tag tag : criticTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.CRITIC, tag, testAccount2, false, "비평게시물" + i + "번째 태그 :" + tag + "내용");
            }
            for (Tag tag : criticTagList) {
                initServiceGenerator.articleGenerator("테스트 티이틀 이미지있음" + i + "번째",
                        Category.CRITIC, tag, testAccount2, true, "비평게시물" + i + "번째 태그 :" + tag + "내용에 이미지 있음");
            }
        }
        List<Tag> filmUniverseTagList = Arrays.stream(Tag.values()) //게시판 태그만 추출
                .filter(tag -> tag.getCategory() == Category.FILM_UNIVERSE)
                .toList();
        File filmUniverseFile = initServiceGenerator.fileGenerator("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png", "original_file_name21");
        //film universe 생성
        for (int i = 0; i < 20; i++) {
            for (Tag tag : filmUniverseTagList) {
                Article article = initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.FILM_UNIVERSE, tag, testAccount, false, "필름유니버스" + i + "번째 태그 :" + tag + "내용");
                initServiceGenerator.filmUniverseGenerator(filmUniverseFile, article, LocalDateTime.of(2023, 4, 1, 0, 0, 0).plusDays(i), LocalDateTime.of(2023, 4, 30, 0, 0, 0).plusDays(i));
            }
            for (Tag tag : filmUniverseTagList) {
                Article article = initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.FILM_UNIVERSE, tag, testAccount, true, "필름유니버스" + i + "번째 태그 :" + tag + "내용에 이미지 있음");
                initServiceGenerator.filmUniverseGenerator(filmUniverseFile, article, LocalDateTime.of(2023, 4, 1, 0, 0, 0).plusDays(i), LocalDateTime.of(2023, 4, 30, 0, 0, 0).plusDays(i));
            }
            for (Tag tag : filmUniverseTagList) {
                Article article = initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.FILM_UNIVERSE, tag, testAccount2, true, "필름유니버스" + i + "번째 태그 :" + tag + "내용에 이미지 있음");
                initServiceGenerator.filmUniverseGenerator(filmUniverseFile, article, LocalDateTime.of(2023, 4, 1, 0, 0, 0).plusDays(i), LocalDateTime.of(2023, 4, 30, 0, 0, 0).plusDays(i));
            }
            for (Tag tag : filmUniverseTagList) {
                Article article = initServiceGenerator.articleGenerator("테스트 티이틀" + i + "번째",
                        Category.FILM_UNIVERSE, tag, testAccount2, false, "필름유니버스" + i + "번째 태그 :" + tag + "내용");
                initServiceGenerator.filmUniverseGenerator(filmUniverseFile, article, LocalDateTime.of(2023, 4, 1, 0, 0, 0).plusDays(i), LocalDateTime.of(2023, 4, 30, 0, 0, 0).plusDays(i));
            }
        }


    }
}
