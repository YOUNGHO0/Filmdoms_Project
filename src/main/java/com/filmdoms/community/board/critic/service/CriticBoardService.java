package com.filmdoms.community.board.critic.service;

import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.critic.data.dto.request.post.CriticBoardPostRequestDto;
import com.filmdoms.community.board.critic.data.entity.CriticBoardHeader;
import com.filmdoms.community.board.critic.repository.CriticBoardHeaderRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.board.review.data.dto.request.post.MovieReviewPostDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewContent;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CriticBoardService {

        private final AccountRepository accountRepository;
        private final CriticBoardHeaderRepository criticBoardHeaderRepository;
        private final ImageFileRepository imageFileRepository;
        private final AmazonS3Upload amazonS3Upload;



        public Response writeMovieReview(CriticBoardPostRequestDto dto, MultipartFile multipartFile) {

        log.info("영화 작성 시작");

        CriticBoardHeader criticBoardHeader = CriticBoardHeader.builder()
                .preHeader(dto.getPreHeader())
                .title(dto.getTitle())
                .content(new BoardContent(dto.getContent()))
                .author(accountRepository.findByUsername(dto.getAuthor()).get())
                .build();


        log.info("영화 작성 시작2");

            criticBoardHeaderRepository.save(criticBoardHeader);


        String uuidFileName = null;
        String originalFileName = null;
        if (multipartFile != null) {
            uuidFileName = UUID.randomUUID().toString();
            originalFileName = multipartFile.getOriginalFilename();
            log.info("영화 작성 시작3");

            try {
                String url =  amazonS3Upload.upload(multipartFile, uuidFileName, originalFileName);
                ImageFile imageFile = new ImageFile(uuidFileName, originalFileName,url,criticBoardHeader);
                imageFileRepository.save(imageFile);
                log.info("이미지업로드완료");
                return Response.success(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        log.info("영화 작성 시작4");

        return Response.success();

    }

    public String readCriticBoard()
    {

        return "";
    }

    public String updateCriticBoard()
    {

        return "";
    }

    public String deleteCriticBoard()
    {

        return "";
    }



}
