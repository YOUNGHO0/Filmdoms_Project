package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticBoardHeader extends BoardHeadCore {


    String preHeader;

    @OneToOne
    @JoinColumn(name = "image_file_id")
    private ImageFile mainImage;

    @Builder
    public CriticBoardHeader(String title, Account author, PostStatus postStatus, ImageFile mainImage ,BoardContent content,String preHeader)
    {
        super(title,author,content);
        this.preHeader =preHeader;
        this.mainImage = mainImage;

    }


    public void updateCriticBoard(String title, String content,String preHeader, ImageFile mainImage)
    {
        super.updateBoardHeadCore(title,content);
        this.preHeader = preHeader;
        this.mainImage = mainImage;


    }


}
