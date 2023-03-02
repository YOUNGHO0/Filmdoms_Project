package com.filmdoms.community.board.critic.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardUpdateRequestDto;
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardGetResponseDto;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.critic.repository.CriticBoardHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.dto.ImageFileDto;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public Response writeCritic(CriticBoardPostRequestDto dto) {

        log.info("영화 작성 시작");
        BoardContent criticBoardContent = BoardContent.builder().content(dto.getContent()).build();
        CriticBoardHeader criticBoardHeader = CriticBoardHeader.builder()
                .preHeader(dto.getPreHeader())
                .title(dto.getTitle())
                .content(criticBoardContent)
                .author(accountRepository
                        .findByUsername(dto.getAuthor())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND)))
                // 메인 이미지 설정
                .mainImage(imageFileRepository.getReferenceById(dto.getMainImageId()))
                .build();

        log.info("영화 작성 시작2");

        criticBoardHeaderRepository.save(criticBoardHeader);

        imageFileService.setImageContent(dto.getMainImageId(), criticBoardContent);
        imageFileService.setImageContent(dto.getContentImageId(),criticBoardContent);


        return Response.success("sucess했음");


    }

    public List<CriticBoardGetResponseDto> getCriticBoardList() {
        List<CriticBoardHeader> resultBoard = criticBoardHeaderRepository.getBoardList();
        List<BoardContent> boardcontents = resultBoard.stream().map(criticBoardHeader -> criticBoardHeader.getBoardContent()).collect(Collectors.toList());

        List<ImageFile> imageFiles = imageFileRepository.getImageFiles(boardcontents);

        HashMap<Long, List<String>> imageFileHashMap = new HashMap<>();
        List<CriticBoardGetResponseDto> responseDtoList = new ArrayList<>();
        setImageUrltoHeader (resultBoard, imageFiles, imageFileHashMap, responseDtoList);
        return responseDtoList;


    }

    private void setImageUrltoHeader (List<CriticBoardHeader> resultBoard, List<ImageFile> imageFiles, HashMap<Long, List<String>> imageFileHashMap, List<CriticBoardGetResponseDto> responseDtoList) {

        // 해시맵을 게시판의 id를 키로 하여 value 를List<String> 즉 url 리스트로 가지는 해시맵 생성
        //초기 해시맵 세팅
        resultBoard.stream()
                .forEach(criticBoardHeader -> imageFileHashMap.put(criticBoardHeader.getId(), new ArrayList<>()));
        //이미지 파일 모으기
        // 해당하는 게시판 번호로 이미지 파일 맵에 넣어주기
        imageFiles.stream()
                .map(imageFile -> ImageFileDto.from(imageFile, domain))
                .forEach(dto -> imageFileHashMap.get(dto.getContentId()).add(dto.getFileUrl()));
        // 최종적으로 dto에 이미지 파일 리스트를 넣어주기
        //responseDtoList 리스트에 add
        resultBoard.stream().forEach(criticBoardHeader -> responseDtoList.add(
                CriticBoardGetResponseDto.from(criticBoardHeader, imageFileHashMap)));
    }


    public Response updateCriticBoard(CriticBoardUpdateRequestDto dto)
    {
        CriticBoardHeader criticBoard = criticBoardHeaderRepository.getCriticBoardHeaderWithBoardContent(dto.getId());
        ImageFile mainImageFile = imageFileRepository.findById(dto.getMainImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        //이미지 파일 업데이트 코드(헤더쪽)
        criticBoard.updateCriticBoard(dto.getTitle(), dto.getContent(), dto.getPreHeader(),mainImageFile);
        //이미지 파일 업데이트 코드 (이미지 파일쪽)
        imageFileService.setImageContent(dto.getMainImageId(), criticBoard.getBoardContent());
        imageFileService.updateImageContent(dto.getContentImageId(),criticBoard.getBoardContent());
        
        return Response.success();
    }


    @Transactional
    public String deleteCriticBoard(Long id)
    {
        CriticBoardHeader criticBoardHeader = criticBoardHeaderRepository.findById(id).get();
        criticBoardHeaderRepository.delete(criticBoardHeader);
        return "sucess";
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
