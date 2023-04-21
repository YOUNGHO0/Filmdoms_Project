package com.filmdoms.community.comment.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentVoteResponseDto {

    private Boolean isVoted;
    private Integer voteCount;
}
