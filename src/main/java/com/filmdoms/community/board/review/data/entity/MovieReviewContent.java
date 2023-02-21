package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MovieReviewContent")
@Table(name = "\"movie_review_content\"")
@NoArgsConstructor
@SuperBuilder
public class MovieReviewContent extends BoardContent {




   public MovieReviewContent(String contentName){
       super(contentName);
    }




}
