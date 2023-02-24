package com.filmdoms.community.board.data;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "board_content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class BoardContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    @Builder
    private BoardContent(String content) {
        this.content = content;
    }


    public void updateBoardContent(String content)
    {
        this.content = content;
    }
}
