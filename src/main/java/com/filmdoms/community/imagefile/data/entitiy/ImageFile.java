package com.filmdoms.community.imagefile.data.entitiy;


import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Getter
public class ImageFile extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String originalFileName;
    String uuidFileName;
    String fileUrl;


    @ManyToOne
    @JoinColumn(name = "board_head_core_id")
    public BoardHeadCore boardHeadCore;


    public ImageFile(String uuidFileName, String originalFileName, String fileUrl, BoardHeadCore boardHeadCore) {
        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
        this.boardHeadCore = boardHeadCore;
    }
}
