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
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3Upload;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CriticBoardService {

        private final AccountRepository accountRepository;
        private final CriticBoardHeaderRepository criticBoardHeaderRepository;
        private final ImageFileRepository imageFileRepository;
        private final AmazonS3Upload amazonS3Upload;

        @PersistenceContext
        private  final EntityManager em;



        public Response writeCritic(CriticBoardPostRequestDto dto, List<MultipartFile> multipartFileList) {

        log.info("영화 작성 시작");

        CriticBoardHeader criticBoardHeader = CriticBoardHeader.builder()
                .preHeader(dto.getPreHeader())
                .title(dto.getTitle())
                .content(BoardContent.builder().content(dto.getContent()).build())
                .author(accountRepository
                        .findByUsername(dto.getAuthor())
                        .orElseThrow(() -> new ApplicationException(ErrorCode.URI_NOT_FOUND)))
                .build();


        log.info("영화 작성 시작2");

        criticBoardHeaderRepository.save(criticBoardHeader);


        List<String> urlList = new ArrayList<>();
        if(multipartFileList != null)
        {
            log.info("멑티파일리스트");
            multipartFileList.stream().forEach(multipartFile ->
            {
                String originalFileName = null;
                String uuidFileName = UUID.randomUUID().toString();
                originalFileName = multipartFile.getOriginalFilename();
                log.info("멀티파트리스트 사이즈{}",multipartFileList.size());
                String url = null;
                try {
                    url = amazonS3Upload.upload(multipartFile, uuidFileName, originalFileName);
                    ImageFile imageFile = ImageFile.from(uuidFileName, originalFileName,criticBoardHeader, url);
                    imageFileRepository.save(imageFile);
                    if(url!=null)
                        log.info("파일 업로드 성공");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            });

        }


        return Response.success("sucess했음");


    }

    public List<CriticBoardGetResponseDto> getCriticBoardList()
    {
        List<CriticBoardHeader> resultBoard = getCriticBoardHeaders();
        List<ImageFile> imageFiles = getImageFiles(resultBoard);

        HashMap<Long, List<String>> imageFileHashMap = new HashMap<>();
        List<CriticBoardGetResponseDto> responseDtoList = new ArrayList<>();

        setImageFilesToCriticBoardHeader(resultBoard, imageFiles, imageFileHashMap, responseDtoList);

        return responseDtoList;


    }



//    public Response updateCriticBoard(CriticBoardUpdateRequestDto dto, List<MultipartFile> multipartFiles)
//    {
//        TypedQuery<CriticBoardHeader> query = em.createQuery("SELECT c from CriticBoardHeader c join fetch c.boardContent where c.id =:id", CriticBoardHeader.class);
//        CriticBoardHeader criticBoard = query.setParameter("id", dto.getBoardNumber()).getSingleResult();
//        criticBoard.updateCriticBoard(dto.getTitle(), dto.getContent(), dto.getPreHeader());
//
//        return Response.success(criticBoard);
//    }

    public String deleteCriticBoard()
    {

        return "";
    }

    @PostConstruct
    public void setInitalData()
    {
        Account author = Account.of("user1", "1234", AccountRole.USER);
        accountRepository.save(author);

    }

    private List<ImageFile> getImageFiles(List<CriticBoardHeader> resultBoard) {
        List<ImageFile> imageFiles = em.createQuery(
                        "SELECT i FROM ImageFile i WHERE i.boardHeadCore IN (?1)", ImageFile.class)
                .setParameter(1, resultBoard).getResultList();
        return imageFiles;
    }

    private List<CriticBoardHeader> getCriticBoardHeaders() {
        List<CriticBoardHeader> resultBoard = em.createQuery("SELECT c from CriticBoardHeader c " +
                "join fetch c.boardContent " +
                "join fetch c.author " +
                "order by c.id desc  limit 5", CriticBoardHeader.class).getResultList();
        return resultBoard;
    }

    private  void setImageFilesToCriticBoardHeader(List<CriticBoardHeader> resultBoard, List<ImageFile> imageFiles, HashMap<Long, List<String>> imageFileHashMap, List<CriticBoardGetResponseDto> responseDtoList) {
        //초기 해시맵 셋팅
        resultBoard.stream().forEach(criticBoardHeader -> imageFileHashMap.put(criticBoardHeader.getId(),new ArrayList<>()));
        //이미지 파일 모으기
        imageFiles.stream().forEach(imageFile -> imageFileHashMap.get(imageFile.boardHeadCore.getId()).add(imageFile.getFileUrl()));
        //list에 add
        resultBoard.stream().forEach(criticBoardHeader -> responseDtoList.add( CriticBoardGetResponseDto.from(criticBoardHeader, imageFileHashMap)));
    }



}
