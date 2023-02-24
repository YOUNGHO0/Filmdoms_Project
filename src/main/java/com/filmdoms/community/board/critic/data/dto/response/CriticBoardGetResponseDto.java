package com.filmdoms.community.board.critic.data.dto.response;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
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

    public static CriticBoardGetResponseDto from (CriticBoardHeader criticBoardHeader, HashMap<Long, List<String>> hashMap )
    {
        CriticBoardGetResponseDto criticBoardGetResponseDto = CriticBoardGetResponseDto.builder()
                .id(criticBoardHeader.getId())
                .preHeader(criticBoardHeader.getPreHeader())
                .title(criticBoardHeader.getTitle())
                .author(criticBoardHeader.getAuthor().getUsername())
                .imageUrl(hashMap.get(criticBoardHeader.getId())).build();

        return criticBoardGetResponseDto;

    }
}
