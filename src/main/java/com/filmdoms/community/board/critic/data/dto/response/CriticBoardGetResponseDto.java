package com.filmdoms.community.board.critic.data.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CriticBoardGetResponseDto {

    Long id;
    String preHeader;
    String title;
    String author;
    List<String> imageUrl;


    @Builder
    public CriticBoardGetResponseDto(Long id,String preHeader,String title,String author,List<String> imageUrl){
        this.id = id;
        this.preHeader = preHeader;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
    }
}
