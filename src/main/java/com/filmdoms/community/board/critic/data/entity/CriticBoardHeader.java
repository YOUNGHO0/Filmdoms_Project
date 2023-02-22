package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.post.data.entity.Post;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
public class CriticBoardHeader extends BoardHeadCore {


    String preHeader;


    public CriticBoardHeader(String title, Account author, PostStatus postStatus, BoardContent content,String preHeader)
    {
        super(title,author,content);
        this.preHeader =preHeader;

    }

    public CriticBoardHeader() {

    }
}
