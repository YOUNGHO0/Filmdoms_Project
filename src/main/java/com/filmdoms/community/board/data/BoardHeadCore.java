package com.filmdoms.community.board.data;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_head_core")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardHeadCore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    private int view = 0;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @JoinColumn(name = "board_content_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BoardContent boardContent;

    protected BoardHeadCore(String title, Account author, BoardContent boardContent) {
        this.title = title;
        this.author = author;
        this.boardContent = boardContent;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
