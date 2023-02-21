package com.filmdoms.community.board.data;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@SuperBuilder
@Getter
public class BoardHeadCore extends  BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @Builder.Default
    private int view = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @JoinColumn(name = "movie_review_content_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BoardContent content;



    public BoardHeadCore(String title, Account author, BoardContent content) {

        this.title = title;
        this.author = author;
        this.content = content;
    }

    public BoardHeadCore() {

    }
}
