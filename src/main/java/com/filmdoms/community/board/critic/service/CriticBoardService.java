package com.filmdoms.community.board.critic.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardDeleteRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardReplyRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardUpdateRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
import com.filmdoms.community.board.critic.data.entity.CriticBoardComment;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.critic.repository.CriticBoardHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.dto.ImageFileDto;
import com.filmdoms.community.imagefile.data.dto.UploadedFileDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3UploadService;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CriticBoardService {

        @Value("${domain}")
        private String domain;
        private final AccountRepository accountRepository;
        private final CriticBoardHeaderRepository criticBoardHeaderRepository;
        private final ImageFileRepository imageFileRepository;
        private final ImageFileService imageFileService;

        @PersistenceContext
        private  final EntityManager em;


    public Response writeCritic(CriticBoardPostRequestDto dto, List<MultipartFile> multipartFileList) {

        log.info("영화 작성 시작");
        BoardContent criticBoardContent = BoardContent.builder().content(dto.getContent()).build();
        CriticBoardHeader criticBoardHeader = CriticBoardHeader.builder()
                .preHeader(dto.getPreHeader())
                .title(dto.getTitle())
                .content(criticBoardContent)
                .author(accountRepository
                        .findByUsername(dto.getAuthor())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND)))
                .build();

        log.info("영화 작성 시작2");

        criticBoardHeaderRepository.save(criticBoardHeader);

        if(multipartFileList != null)
        {
           // imageFileService.saveImages(multipartFileList,criticBoardHeader);

        }


        return Response.success("sucess했음");


    }

    public List<CriticBoardGetResponseDto> getCriticBoardList() {
        List<CriticBoardHeader> resultBoard = criticBoardHeaderRepository.getBoardList();
        List<ImageFile> imageFiles = imageFileRepository.getImageFiles(resultBoard);

        HashMap<Long, List<String>> imageFileHashMap = new HashMap<>();
        List<CriticBoardGetResponseDto> responseDtoList = new ArrayList<>();

        setImageFilesToCriticBoardHeader(resultBoard, imageFiles, imageFileHashMap, responseDtoList);

        return responseDtoList;


    }



    public Response updateCriticBoard(CriticBoardUpdateRequestDto dto)
    {
        CriticBoardHeader criticBoard = criticBoardHeaderRepository.getCriticBoardHeaderWithBoardContent(dto.getId());
        criticBoard.updateCriticBoard(dto.getTitle(), dto.getContent(), dto.getPreHeader());

        return Response.success();
    }


    public String deleteCriticBoard(Long id)
    {
        CriticBoardHeader criticBoardHeader = criticBoardHeaderRepository.findById(id).get();
        criticBoardHeaderRepository.delete(criticBoardHeader);
        return "sucess";
    }






    private void setImageFilesToCriticBoardHeader(List<CriticBoardHeader> resultBoard, List<ImageFile> imageFiles,
                                                  HashMap<Long, List<String>> imageFileHashMap,
                                                  List<CriticBoardGetResponseDto> responseDtoList) {
        //초기 해시맵 셋팅
        resultBoard.stream()
                .forEach(criticBoardHeader -> imageFileHashMap.put(criticBoardHeader.getId(), new ArrayList<>()));
        //이미지 파일 모으기
        imageFiles.stream()
                .map(imageFile -> ImageFileDto.from(imageFile, domain))
                .forEach(dto -> imageFileHashMap.get(dto.getId()).add(dto.getFileUrl()));
        //responseDtoList 리스트에 add
        resultBoard.stream().forEach(criticBoardHeader -> responseDtoList.add(
                CriticBoardGetResponseDto.from(criticBoardHeader, imageFileHashMap)));
    }



    @PostConstruct
    public void test()
    {
        Account author = Account.builder()
                .username("user1").password("123456").role(AccountRole.USER).build();
        accountRepository.save(author);
        //저장 되는지 확인
    }


}
