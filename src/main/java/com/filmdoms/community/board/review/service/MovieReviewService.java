package com.filmdoms.community.board.review.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.response.Response;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.review.data.dto.request.post.MovieReviewPostDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewMainPageDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewComment;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.review.repository.MovieReviewCommentRepository;
import com.filmdoms.community.board.review.repository.MovieReviewHeaderRepository;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.AmazonS3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewService {

    private final MovieReviewHeaderRepository headerRepository;
    private final MovieReviewCommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final ImageFileRepository imageFileRepository;
    private final AmazonS3Upload amazonS3Upload;


    public List<MovieReviewMainPageDto> getMainPageDtos() {
        return headerRepository.findTop5ByOrderByDateCreatedDesc()
                .stream()
                .map(MovieReviewMainPageDto::new)
                .collect(Collectors.toList());
    }


    public Response writeMovieReview(MovieReviewPostDto dto, MultipartFile multipartFile) {

        log.info("영화 작성 시작");

        MovieReviewHeader header = MovieReviewHeader.builder()
                .tag(MovieReviewTag.A)
                .title(dto.getTitle())
                .author(accountRepository.findByUsername(dto.getAuthor()).get())
                .content(BoardContent.builder().content(dto.getContent()).build())
                .build();
        log.info("영화 작성 시작2");

        headerRepository.save(header);


        String uuidFileName = null;
        String originalFileName = null;
        if (multipartFile != null) {
            uuidFileName = UUID.randomUUID().toString();
            originalFileName = multipartFile.getOriginalFilename();
            log.info("영화 작성 시작3");

            try {
              String url =  amazonS3Upload.upload(multipartFile, uuidFileName, originalFileName);
              ImageFile imageFile = new ImageFile(uuidFileName, originalFileName,url,header);
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

    public void initData() throws InterruptedException {
        Account author = Account.of("user1", "1234", AccountRole.USER);
        accountRepository.save(author);

        for(int i = 0; i < 10; i++) {
            MovieReviewHeader header = MovieReviewHeader.builder()
                    .tag(MovieReviewTag.A)
                    .title("review " + i)
                    .author(author)
                    .content(BoardContent.builder().content("test_content").build())
                    .build();
            headerRepository.save(header);
            Thread.sleep(10);
        }
        List<MovieReviewHeader> result = headerRepository.findTop5ByOrderByDateCreatedDesc();
        for(int i = 0; i < result.size(); i++) {
            MovieReviewHeader header = result.get(i);
            for(int j = 0; j < i % 2 + 1; j++) {
                MovieReviewComment comment = MovieReviewComment.builder()
                        .header(header)
                        .content("test comment")
                        .build();
                commentRepository.save(comment);
            }
        }
    }

}
