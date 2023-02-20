package com.filmdoms.community.post.data.dto;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.post.data.constants.PostCategory;
import com.filmdoms.community.post.data.entity.Post;
import com.filmdoms.community.postComment.data.dto.PostCommentDto;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDetailDto {

    private Long id;
    private AccountDto accountDto;
    private PostCategory postCategory;
    private String title;
    private String content;
    private Integer view;
    private Set<PostCommentDto> postComments;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public static PostDetailDto from(Post entity) {
        return new PostDetailDto(
                entity.getId(),
                AccountDto.from(entity.getAccount()),
                entity.getPostCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getView(),
                entity.getPostComments().stream()
                        .map(PostCommentDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getDateCreated(),
                entity.getDateLastModified()
        );
    }
}
