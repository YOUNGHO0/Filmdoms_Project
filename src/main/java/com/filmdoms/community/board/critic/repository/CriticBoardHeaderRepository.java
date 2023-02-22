package com.filmdoms.community.board.critic.repository;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.post.data.entity.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CriticBoardHeaderRepository extends JpaRepository<CriticBoardHeader,Long> {





}
