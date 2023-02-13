package com.filmdoms.community.imagefile.data.entitiy;


import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardContentCore;
import com.filmdoms.community.board.review.data.entity.MovieReviewContent;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class ImageFile extends BaseTimeEntity {


    @Id @GeneratedValue
    Long id;
    String originalFileName;
    String uuidFileName;
    String fileUrl;


    @ManyToOne
    @JoinColumn(name = "content_id")
    private BoardContentCore boardContentCore;


    public ImageFile(String uuidFileName, String originalFileName, String fileUrl, BoardContentCore boardContentCore) {
        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
        this.boardContentCore = boardContentCore;
    }
}
