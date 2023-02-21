package com.filmdoms.community.board.data;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Data
public class BoardContent extends BaseTimeEntity {

    @Id @GeneratedValue
    Long id;
    String content;



}
