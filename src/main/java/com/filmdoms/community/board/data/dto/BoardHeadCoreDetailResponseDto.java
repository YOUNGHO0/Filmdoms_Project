package com.filmdoms.community.board.data.dto;

import com.filmdoms.community.account.data.dto.response.SimpleAccountResponseDto;
import com.filmdoms.community.board.data.BoardHeadCore;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.imagefile.data.dto.response.ImageResponseDto;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public abstract class BoardHeadCoreDetailResponseDto {
    private Long id;
    private String title;
    private SimpleAccountResponseDto author;
    private int view;
    private PostStatus status;
    private String content;
    private Set<ImageResponseDto> image;

    protected BoardHeadCoreDetailResponseDto(BoardHeadCore boardHeadCore) {
        this.id = boardHeadCore.getId();
        this.title = boardHeadCore.getTitle();
        this.author = new SimpleAccountResponseDto(boardHeadCore.getAuthor());
        this.view = boardHeadCore.getView();
        this.status = boardHeadCore.getStatus();
        this.content = boardHeadCore.getBoardContent().getContent();
        this.image = boardHeadCore.getBoardContent().getImageFiles().stream()
                .map(ImageResponseDto::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
