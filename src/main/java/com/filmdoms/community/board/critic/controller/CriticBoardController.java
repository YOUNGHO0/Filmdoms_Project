package com.filmdoms.community.board.critic.controller;

import com.filmdoms.community.account.data.dto. *;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardDeleteRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardReplyRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardUpdateRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
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

    @PostMapping("/write")
    public Response<String> writeCritic(@RequestPart CriticBoardPostRequestDto criticBoardPostRequestDto, @RequestPart(required = false) List<MultipartFile> multipartFile) {

        return criticBoardService.writeCritic(criticBoardPostRequestDto, multipartFile);
    }

    @GetMapping("/list")
    public Response getCriticBoardList() {
        List<CriticBoardGetResponseDto> criticBoardList = criticBoardService.getCriticBoardList();

        for (int i = 0; i < criticBoardList.size(); i++) {
            log.info("게시글 목록{}", criticBoardList.get(i).getTitle());
        }
        return Response.success(criticBoardList);


    }

    @PutMapping("/update")
    public Response updateCriticBoard( @AuthenticationPrincipal AccountDto accountDto,
                                       @PathVariable Long postId,
                                       @RequestBody CriticBoardUpdateRequestDto requestDto)
    {
        Response response = criticBoardService.updateCriticBoard(requestDto);

        return response;

    }

    @DeleteMapping("/delete")
    public Response deleteCriticBoard( @AuthenticationPrincipal AccountDto accountDto, @PathVariable Long postId)
    {
        criticBoardService.deleteCriticBoard(postId);

        return Response.success();
    }


//    @PostMapping("/{id}/reply")
//    public Response writeReply(@RequestBody CriticBoardReplyRequestDto dto)
//    {
//        criticBoardService.writeReply(dto)
//
//    }


}
