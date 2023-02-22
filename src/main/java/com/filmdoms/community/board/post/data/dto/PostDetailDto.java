package com.filmdoms.community.board.post.data.dto;

import com.filmdoms.community.board.post.data.constants.PostCategory;
import com.filmdoms.community.board.post.data.entity.PostHeader;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDetailDto {

    private Long id;
    private PostAccountDto author;
    private PostCategory postCategory;
    private String title;
    private String content;
    private Integer view;
    private Set<PostCommentDto> postComments;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;

    public static PostDetailDto from(PostHeader entity) {
        return new PostDetailDto(
                entity.getId(),
                PostAccountDto.from(entity.getAuthor()),
                entity.getCategory(),
                entity.getTitle(),
                entity.getBoardContent().getContent(),
                entity.getView(),
                entity.getComments().stream()
                        .map(PostCommentDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getDateCreated(),
                entity.getDateLastModified()
        );
    }
}
