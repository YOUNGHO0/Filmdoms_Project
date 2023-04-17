package com.filmdoms.community.newcomment.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewCommentVoteResponseDto {

    private Boolean isVoted;
    private Integer voteCount;
}
