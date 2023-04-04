package com.filmdoms.community.vote.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.vote.data.dto.VoteResponseDto;
import com.filmdoms.community.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/{articleId}/vote")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping()
    public Response<VoteResponseDto> toggleVote(
            @PathVariable Long articleId,
            @AuthenticationPrincipal AccountDto accountDto) {
        return Response.success(voteService.toggleVote(accountDto, articleId));
    }
}
