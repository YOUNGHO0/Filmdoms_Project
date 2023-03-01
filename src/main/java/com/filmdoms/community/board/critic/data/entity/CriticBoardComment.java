package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BoardHeadCore;
import lombok.Builder;

public class CriticBoardComment extends CommentCore {



    @Builder
    public CriticBoardComment(String content, CriticBoardHeader criticBoardHeader, Account account)
    {
        super(content,criticBoardHeader, account);

    }

}



