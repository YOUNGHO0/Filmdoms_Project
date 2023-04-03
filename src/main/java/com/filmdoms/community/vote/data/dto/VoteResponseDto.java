package com.filmdoms.community.vote.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VoteResponseDto {

    private Boolean isVoted;

    private Integer voteCount;
}
