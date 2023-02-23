package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 게시판에서 조회할 땐 Comment 내용을 조회하지 않아도 되므로, DB 조회가 일어나지 않도록 했다.
 */
@Getter
@Builder
@AllArgsConstructor
public class PostBriefDto {

    private Long id;
    private PostAccountDto author;
    private PostCategory postCategory;
    private String title;
    private Integer view;
    private Integer commentCount;
    private LocalDateTime dateCreated;

    public static PostBriefDto from(PostHeader entity) {
        return new PostBriefDto(
                entity.getId(),
                PostAccountDto.from(entity.getAuthor()),
                entity.getCategory(),
                entity.getTitle(),
                entity.getView(),
                entity.getComments().size(),
                entity.getDateCreated()
        );
    }
}
