package com.filmdoms.community.board.critic.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;

import java.util.List;

public interface CriticBoardHeaderCustom {


    List<CriticBoardHeader> getBoardList();

    CriticBoardHeader getCriticBoardHeaderWithBoardContent(Long id);





}
