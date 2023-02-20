package com.filmdoms.community.board.review.data.entity;

import com.filmdoms.community.board.data.BoardContentCore;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MovieReviewContent")
@Table(name = "\"movie_review_content\"")
@NoArgsConstructor
@Data
public class MovieReviewContent extends BoardContentCore {


    @OneToMany(mappedBy = "boardContentCore" )
    public List<ImageFile> imageFiles = new ArrayList<>();



   public MovieReviewContent(String contentName){
       this.setContent(contentName);
    }




}
