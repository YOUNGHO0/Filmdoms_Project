package com.filmdoms.community.board.data;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.data.constant.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardHeadCore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    private int view = 0;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.ACTIVE;

    @JoinColumn(name = "board_content_core_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BoardContent content;

    protected BoardHeadCore(String title, Account author, BoardContent content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
