package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @Builder
    public CriticBoardHeader(String title, Account author, PostStatus postStatus, BoardContent boardContent,String preHeader)
    {
        super(title,author,boardContent);
        this.preHeader =preHeader;

    }

    @OneToMany(mappedBy = "boardHeadCore", cascade = CascadeType.ALL)
    List<ImageFile> imageFileList = new ArrayList<>();

    public void updateCriticBoard(String title, String content,String preHeader)
    {
        super.updateBoardHeadCore(title,content);
        this.preHeader = preHeader;


    }


}
