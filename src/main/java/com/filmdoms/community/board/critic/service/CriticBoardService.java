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
import com.filmdoms.community.board.critic.data.dto.response.CriticBoardSinglePageResponseDto;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.critic.repository.CriticBoardHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        criticBoardHeaderRepository.save(criticBoardHeader);
        imageFileService.setImageContent(dto.getContentImageId(),criticBoardContent);


        return Response.success("sucess했음");


    }

    public List<CriticBoardGetResponseDto> getCriticBoardLists() {

        List<CriticBoardHeader> boardList = criticBoardHeaderRepository.findCriticBoardHeaderWithAuthorContentMainImage();
        List<CriticBoardGetResponseDto> dto = boardList.stream().map(criticBoardHeader -> CriticBoardGetResponseDto.from(criticBoardHeader, domain)).collect(Collectors.toList());

        return dto;
    }

    public CriticBoardSinglePageResponseDto getSingleCriticBoardPage(Long id) {

        CriticBoardHeader singlePage = criticBoardHeaderRepository.findCriticBoardHeaderWithAuthorContentMainImageById(id);
        return CriticBoardSinglePageResponseDto.from(singlePage,domain);
    }


    public Response updateCriticBoard(CriticBoardUpdateRequestDto dto)
    {

        ImageFile mainImageFile = imageFileRepository.findById(dto.getMainImageId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_IMAGE_ID));
        //이미지 파일 업데이트 코드(헤더쪽)
        CriticBoardHeader criticBoard = criticBoardHeaderRepository.findCriticBoardHeaderWithAuthorContentMainImageById(dto.getId());
        criticBoard.updateCriticBoard(dto.getTitle(), dto.getContent(), dto.getPreHeader(),mainImageFile);
        //이미지 파일 업데이트 코드 (이미지 파일쪽)
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











//    @PostConstruct
//    public void test()
//    {
//        Account author = Account.builder()
//                .username("user1").password("123456").role(AccountRole.USER).build();
//        accountRepository.save(author);
//        //저장 되는지 확인
//    }


}
