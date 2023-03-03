package com.filmdoms.community.board.critic.controller;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardUpdateRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardSinglePageResponseDto;
import com.filmdoms.community.board.critic.service.CriticBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/critic")
public class CriticBoardController {

    private final CriticBoardService criticBoardService;

    @PostMapping
    public Response<String> writeCritic( @AuthenticationPrincipal AccountDto accountDto, @RequestBody CriticBoardPostRequestDto requestDto) {

        return criticBoardService.writeCritic(requestDto);
    }

    @GetMapping
    public Response getCriticBoardList() {
        List<CriticBoardGetResponseDto> criticBoardList = criticBoardService.getCriticBoardLists();

        return Response.success(criticBoardList);


    }



    @PutMapping("/{id}")
    public Response updateCriticBoard( @AuthenticationPrincipal AccountDto accountDto,
                                       @RequestBody CriticBoardUpdateRequestDto requestDto, @PathVariable("id") Long id)
    {
        requestDto.setId(id);

        Response response = criticBoardService.updateCriticBoard(requestDto);

        return response;

    }

    @DeleteMapping("/{id}")
    public Response deleteCriticBoard( @AuthenticationPrincipal AccountDto accountDto ,@PathVariable("id") Long id)
    {
        criticBoardService.deleteCriticBoard(id);
        return Response.success();
    }



}
