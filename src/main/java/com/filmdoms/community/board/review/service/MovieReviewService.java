package com.filmdoms.community.board.review.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.review.data.dto.request.MovieReviewCreateRequestDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewCreateResponseDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewComment;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewMainPageDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.review.repository.MovieReviewCommentRepository;
import com.filmdoms.community.board.review.repository.MovieReviewHeaderRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewService {

    private final MovieReviewHeaderRepository headerRepository;
    private final MovieReviewCommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final ImageFileService imageFileService;

    public List<MovieReviewMainPageDto> getMainPageDtos() {
        return headerRepository.findTop5ByOrderByDateCreatedDesc()
                .stream()
                .map(MovieReviewMainPageDto::new)
                .collect(Collectors.toList());
    }

    public MovieReviewCreateResponseDto createReview(MovieReviewCreateRequestDto requestDto, List<MultipartFile> imageMultipartFiles) throws IOException {
        //인증 로직 필요

        Account author = accountRepository.findById(requestDto.getAccountId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        MovieReviewHeader header = MovieReviewHeader.builder()
                .tag(requestDto.getTag())
                .author(author)
                .title(requestDto.getTitle())
                .content(content)
                .build();

        MovieReviewHeader savedHeader = headerRepository.save(header);

        //이미지 추가 로직
        if(imageMultipartFiles != null) {
            imageFileService.saveImages(imageMultipartFiles, header);
        }

        return new MovieReviewCreateResponseDto(savedHeader);
    }

    public void initData() throws InterruptedException {
        Account author = Account.of("movieReviewUser", "1234", AccountRole.USER);
        accountRepository.save(author);

        for(int i = 0; i < 10; i++) {
            BoardContent content = BoardContent.builder()
                    .content("test content")
                    .build();

            MovieReviewHeader header = MovieReviewHeader.builder()
                    .tag(MovieReviewTag.A)
                    .title("review " + i)
                    .author(author)
                    .content(content)
                    .build();

            headerRepository.save(header);
            Thread.sleep(10);

            //임시 데이터 10개 모두에 댓글 달리도록 수정
            for(int j = 0; j < i % 2 + 1; j++) {
                MovieReviewComment comment = MovieReviewComment.builder()
                        .header(header)
                        .author(author)
                        .content("test comment")
                        .build();
                commentRepository.save(comment);
            }
        }
    }
}
