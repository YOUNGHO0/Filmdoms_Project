package com.filmdoms.community.board.critic.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CriticBoardHeaderRepository extends JpaRepository<CriticBoardHeader,Long> {


    @Query("Select c from CriticBoardHeader  c " +
            "join fetch  c.boardContent " +
            "join fetch c.author  " +
            "join fetch c.mainImage " +
            "order by c.id desc LIMIT 5"
            )
    List<CriticBoardHeader> findCriticBoardHeaderWithAuthorContentMainImage();

    @Query("Select c from CriticBoardHeader  c " +
            "join fetch  c.boardContent " +
            "join fetch c.author  " +
            "join fetch c.boardContent.imageFiles where c.id =:id ")
    CriticBoardHeader findCriticBoardHeaderWithAuthorContentMainImageById(Long id);


}
