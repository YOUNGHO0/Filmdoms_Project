package com.filmdoms.community.board.review.data.dto.request.post;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MovieReviewPostDto {

    String title ;
    String content;
    String author;
    String tag;

    public MovieReviewPostDto(String title, String content, String author, String tag)
    {
        this.title = title;
        this.content =content;
        this.author = author;
        this.tag = tag;


    }


}
