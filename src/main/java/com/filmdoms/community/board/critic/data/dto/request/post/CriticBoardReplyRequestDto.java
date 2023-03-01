package com.filmdoms.community.board.critic.data.dto.request.post;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;

public class CriticBoardReplyRequestDto {

    Long boardNumber;
    String content;
    CriticBoardHeader criticBoardHeader;
    Account account;
}
