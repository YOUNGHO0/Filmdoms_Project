package com.filmdoms.community.post.data.dto;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.post.data.constants.PostCategory;
import com.filmdoms.community.post.data.entity.Post;
import java.sql.Timestamp;
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
    private AccountDto accountDto;
    private PostCategory postCategory;
    private String title;
    private String content;
    private Integer view;
    private Integer postCommentsCount;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public static PostBriefDto from(Post entity) {
        return new PostBriefDto(
                entity.getId(),
                AccountDto.from(entity.getAccount()),
                entity.getPostCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getView(),
                entity.getPostComments().size(),
                entity.getDateCreated(),
                entity.getDateLastModified()
        );
    }
}
