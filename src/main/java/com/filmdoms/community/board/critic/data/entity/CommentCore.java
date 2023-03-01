package com.filmdoms.community.board.critic.data.entity;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.BaseTimeEntity;
import com.filmdoms.community.board.data.BoardHeadCore;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class CommentCore extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @JoinColumn(name = "header")
    @ManyToOne
    private BoardHeadCore boardHeadCore;

    @JoinColumn(name = "comment_author")
    @ManyToOne
    private Account account;


    protected CommentCore( String content, BoardHeadCore boardHeadCore, Account account)
    {
        this.content = content;
        this.boardHeadCore = boardHeadCore;
        this.account = account;
    }





}
