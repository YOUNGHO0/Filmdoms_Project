package com.filmdoms.community.board.data;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
@SuperBuilder
public class BoardContent extends BaseTimeEntity {

    @Id @GeneratedValue
    Long id;
    String content;


    public BoardContent(String content)
    {
        this.content = content;
    }





}
