package com.filmdoms.community.board.data;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardContent extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Builder
    private BoardContent(String content) {
        this.content = content;
    }
}
