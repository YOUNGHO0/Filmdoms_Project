package com.filmdoms.community.board.critic.controller;

import com.filmdoms.community.account.data.dto. *;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardDeleteRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardReplyRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardUpdateRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.critic.service.CriticBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        List<CriticBoardGetResponseDto> criticBoardList = criticBoardService.getCriticBoardList();

        for (int i = 0; i < criticBoardList.size(); i++) {
            log.info("게시글 목록{}", criticBoardList.get(i).getTitle());
        }
        return Response.success(criticBoardList);


    }


    @PutMapping
    public Response updateCriticBoard( @AuthenticationPrincipal AccountDto accountDto,
                                       @RequestBody CriticBoardUpdateRequestDto requestDto)
    {
        Response response = criticBoardService.updateCriticBoard(requestDto);

        return response;

    }

    @DeleteMapping
    public Response deleteCriticBoard( @AuthenticationPrincipal AccountDto accountDto, @RequestBody CriticBoardDeleteRequestDto dto)
    {
        criticBoardService.deleteCriticBoard(dto.getId());
        return Response.success();
    }


//    @PostMapping("/{id}/reply")
//    public Response writeReply(@RequestBody CriticBoardReplyRequestDto dto)
//    {
//        criticBoardService.writeReply(dto)
//
//    }


}
