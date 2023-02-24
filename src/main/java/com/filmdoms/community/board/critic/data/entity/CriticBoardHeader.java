package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticBoardHeader extends BoardHeadCore {


    String preHeader;

    @Builder
    public CriticBoardHeader(String title, Account author, PostStatus postStatus, BoardContent content,String preHeader)
    {
        super(title,author,content);
        this.preHeader =preHeader;

    }


}
