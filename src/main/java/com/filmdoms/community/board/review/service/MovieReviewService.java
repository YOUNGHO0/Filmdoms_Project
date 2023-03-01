package com.filmdoms.community.board.review.service;

import com.filmdoms.community.account.data.dto.AccountDto;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.review.data.dto.request.MovieReviewCreateRequestDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewCreateResponseDto;
import com.filmdoms.community.board.review.data.dto.response.MovieReviewMainPageDto;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.board.review.repository.MovieReviewHeaderRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewService {

    private final MovieReviewHeaderRepository headerRepository;
    private final AccountRepository accountRepository;
    private final ImageFileService imageFileService;

    public List<MovieReviewMainPageDto> getMainPageDtos() {
        return headerRepository.findTop5ByOrderByDateCreatedDesc()
                .stream()
                .map(MovieReviewMainPageDto::new)
                .collect(Collectors.toList());
    }

    public MovieReviewCreateResponseDto create(MovieReviewCreateRequestDto requestDto, AccountDto accountDto) {

        BoardContent content = BoardContent.builder()
                .content(requestDto.getContent())
                .build();

        MovieReviewHeader header = MovieReviewHeader.builder()
                .tag(requestDto.getTag())
                .author(accountRepository.getReferenceById(accountDto.getId()))
                .title(requestDto.getTitle())
                .boardContent(content)
                .build();

        MovieReviewHeader savedHeader = headerRepository.save(header);

        imageFileService.setImageContent(requestDto.getContentImageId(), savedHeader.getBoardContent());

        return new MovieReviewCreateResponseDto(savedHeader);
    }
}
