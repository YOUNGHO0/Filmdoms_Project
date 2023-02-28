package com.filmdoms.community.imagefile.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class ImageFileRepositoryImpl implements ImageFileRepositoryCustom{

    private final EntityManager em;
    @Override
    public List<ImageFile> getImageFiles(List<CriticBoardHeader> resultBoard) {

        List<ImageFile> resultList = em.createQuery(
                        "SELECT i FROM ImageFile i WHERE i.boardHeadCore IN (?1)", ImageFile.class)
                .setParameter(1, resultBoard).getResultList();
        return resultList;


    }
}
