package com.filmdoms.community.board.critic.data.dto.response;

import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CriticBoardSinglePageResponseDto {
    Long id;
    String preHeader;
    String title;
    String author;
    List<String> imageUrl;

    @Builder
    public CriticBoardSinglePageResponseDto(Long id, String preHeader, String title, String author, List<String> imageUrl) {
        this.id = id;
        this.preHeader = preHeader;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
    }

    public static CriticBoardSinglePageResponseDto from(CriticBoardHeader criticBoardHeader, String domain)
    {
        Set<ImageFile> imageFileList = criticBoardHeader.getBoardContent().getImageFiles();
        List<String> fileUrls = imageFileList.stream().map(imageFile -> imageFile.getFileUrl(domain)).collect(Collectors.toList());

        CriticBoardSinglePageResponseDto criticBoardGetResponseDtoDetailDto = CriticBoardSinglePageResponseDto.builder()
                .id(criticBoardHeader.getId())
                .preHeader(criticBoardHeader.getPreHeader())
                .title(criticBoardHeader.getTitle())
                .author(criticBoardHeader.getAuthor().getUsername().toString())
                .imageUrl(fileUrls)
                .build();
        return criticBoardGetResponseDtoDetailDto;
    }
}
