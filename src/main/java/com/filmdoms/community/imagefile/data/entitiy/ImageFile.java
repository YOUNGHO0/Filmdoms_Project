package com.filmdoms.community.imagefile.data.entitiy;


import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFile extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "original_file_name")
    String originalFileName;
    @Column(name = "uuid_file_name")
    String uuidFileName;
    @ManyToOne
    @JoinColumn(name = "board_head_core_id")
    public BoardHeadCore boardHeadCore;


    @Builder
    private ImageFile(String uuidFileName, String originalFileName, BoardHeadCore boardHeadCore) {

        this.uuidFileName = uuidFileName;
        this.originalFileName = originalFileName;
        this.boardHeadCore = boardHeadCore;
    }


}
