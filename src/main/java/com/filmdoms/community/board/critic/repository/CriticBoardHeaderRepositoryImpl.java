package com.filmdoms.community.board.critic.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CriticBoardHeaderRepositoryImpl implements CriticBoardHeaderCustom {

    private  final EntityManager em;
    @Override
    public List<CriticBoardHeader> getBoardList() {

        List<CriticBoardHeader>  resultBoard = em.createQuery("SELECT c from CriticBoardHeader c " +
                "join fetch c.boardContent " +
                "join fetch c.author " +
                "order by c.id desc  limit 5", CriticBoardHeader.class).getResultList();

        return resultBoard;
    }

    @Override
    public CriticBoardHeader getCriticBoardHeaderWithBoardContent(Long id) {

        CriticBoardHeader criticBoardHeader = em.createQuery("SELECT c from CriticBoardHeader c " +
                        "join fetch c.boardContent where c.id =:id",
                CriticBoardHeader.class).setParameter("id",id).getSingleResult();

        return  criticBoardHeader;


    }
}
