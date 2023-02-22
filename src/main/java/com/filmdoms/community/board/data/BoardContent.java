package com.filmdoms.community.board.data;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor

public final class BoardContent  {

    @Id @GeneratedValue
    Long id;
    String content;


    @Builder
    private BoardContent(String content)
    {
        this.content = content;
    }




}
