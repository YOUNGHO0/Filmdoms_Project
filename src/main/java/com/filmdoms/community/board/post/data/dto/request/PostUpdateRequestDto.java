package com.filmdoms.community.board.post.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.CONTENT_NOT_BLANK;
import static com.filmdoms.community.account.exception.ValidationMessage.CONTENT_SIZE;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_NOT_BLANK;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_SIZE;

import com.filmdoms.community.board.post.data.constants.PostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class PostUpdateRequestDto {

    private PostCategory category;

    @NotBlank(message = TITLE_NOT_BLANK)
    @Size(max = 100, message = TITLE_SIZE)
    private String title;

    @NotBlank(message = CONTENT_NOT_BLANK)
    @Size(max = 10000, message = CONTENT_SIZE)
    private String content;

    private Long mainImageId;
    private Set<Long> contentImageId;
}
