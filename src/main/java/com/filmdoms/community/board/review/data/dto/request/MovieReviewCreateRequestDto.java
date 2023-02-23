package com.filmdoms.community.board.review.data.dto.request;

import com.filmdoms.community.board.data.constant.MovieReviewTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class MovieReviewCreateRequestDto {

    private MovieReviewTag tag;
    private String title;
    private Long accountId;
    private String content;
}
