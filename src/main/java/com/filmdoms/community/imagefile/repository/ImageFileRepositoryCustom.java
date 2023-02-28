package com.filmdoms.community.imagefile.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;

import java.util.List;

public interface ImageFileRepositoryCustom {

    List<ImageFile>getImageFiles(List<CriticBoardHeader> resultBoard);
}
