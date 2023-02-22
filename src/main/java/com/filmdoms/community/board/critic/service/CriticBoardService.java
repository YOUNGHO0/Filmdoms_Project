package com.filmdoms.community.board.critic.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
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
                .author(accountRepository.findByUsername(dto.getAuthor()).get())
                .build();


        log.info("영화 작성 시작2");

        criticBoardHeaderRepository.save(criticBoardHeader);
        String uuidFileName = null;
        String originalFileName = null;
        List<String> urlList = new ArrayList<>();
        for(int i=0; i<multipartFileList.size(); i++)
        {

            try {

                    MultipartFile multipartFile = multipartFileList.get(i);
                    uuidFileName = UUID.randomUUID().toString();
                    originalFileName = multipartFile.getOriginalFilename();
                    log.info("멀티파트리스트 사이즈{}",multipartFileList.size());
                    String url =  amazonS3Upload.upload(multipartFile, uuidFileName, originalFileName);
                    ImageFile imageFile = new ImageFile(uuidFileName, originalFileName,url,criticBoardHeader);
                    imageFileRepository.save(imageFile);
                    log.info("이미지업로드완료");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return Response.success("sucess했음");


    }

    public List<CriticBoardGetResponseDto> getCriticBoardList()
    {
        List<CriticBoardHeader> resultBoard = em.createQuery("SELECT c from CriticBoardHeader c join fetch c.boardContent join fetch c.author order by c.id desc  limit 5", CriticBoardHeader.class).getResultList();


        TypedQuery<ImageFile> query = em.createQuery(
                "SELECT i FROM ImageFile i WHERE i.boardHeadCore IN (?1)", ImageFile.class);
        List<ImageFile> imageFiles = query.setParameter(1, resultBoard).getResultList();

        HashMap<Long, List<String>> hashMap = new HashMap<>();
        for(int i=0; i<resultBoard.size(); i++)
        {
            hashMap.put(resultBoard.get(i).getId(),new ArrayList<>());
        }

        for(int i=0; i<imageFiles.size(); i++)
        {

                ImageFile imageFile = imageFiles.get(i);
                hashMap.get(imageFile.boardHeadCore.getId()).add(imageFile.getFileUrl());

        }

        List<CriticBoardGetResponseDto> list = new ArrayList<>();
        for(int i=0; i<resultBoard.size(); i++)
        {
            CriticBoardHeader criticBoardHeader = resultBoard.get(i);
            CriticBoardGetResponseDto criticBoardGetResponseDto = CriticBoardGetResponseDto.builder()
                    .id(criticBoardHeader.getId())
                    .preHeader(criticBoardHeader.getPreHeader())
                    .title(criticBoardHeader.getTitle())
                    .author(criticBoardHeader.getAuthor().getUsername())
                    .imageUrl(hashMap.get(criticBoardHeader.getId())).build();

            list.add(criticBoardGetResponseDto);
        }

        log.info("영화 목록{}",list);
        return list;


    }

    public String updateCriticBoard()
    {


        return "";
    }

    public String deleteCriticBoard()
    {

        return "";
    }

    @PostConstruct
    public void setInitalData()
    {
        // 초기 데이터 작성


    }



}
