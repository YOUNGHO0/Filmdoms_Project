package com.filmdoms.community.article.service;

import com.filmdoms.community.article.data.dto.response.editor.ArticleEditorResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ArticleEditorService {

    String criticNoticeContent = "<p>-------------------------------------------------------------------------------------------------------------------------------------------------------</p><p><span style=\"background-color: rgb(0, 0, 0); color: rgb(255, 255, 255);\">\"" +
            "Critic 게시판은 제목, 내용, 이미지 파일 3개 이상이 필수 입니다. 현재 문구를 삭제해주시고 작성해주세요." +
            "\" \uD83D\uDE0A</span></p><p>-------------------------------------------------------------------------------------------------------------------------------------------------------</p>";

    String filmUniverseNoticeContent = "<p>-------------------------------------------------------------------------------------------------------------------------------------------------------</p><p><span style=\"background-color: rgb(0, 0, 0); color: rgb(255, 255, 255);\">\"" +
            "Film Universe 게시판은 제목, 내용, 모집 기한, 이미지 파일 1개 이상이 필수입니다. 현재 문구를 삭제해주시고 작성해주세요." +
            "\" \uD83D\uDE0A</span></p><p>-------------------------------------------------------------------------------------------------------------------------------------------------------</p>";

    String movieNoticeContent = "";

    public ArticleEditorResponseDto getCriticNotice() {
        return ArticleEditorResponseDto.from(criticNoticeContent);
    }

    public ArticleEditorResponseDto getFilmUniverseNotice() {
        return ArticleEditorResponseDto.from(filmUniverseNoticeContent);
    }

    public ArticleEditorResponseDto getMovieNotice() {
        return ArticleEditorResponseDto.from(movieNoticeContent);
    }

}
