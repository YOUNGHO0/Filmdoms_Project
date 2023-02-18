package com.filmdoms.community.account.service;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.dto.response.review.MovieReviewMainPageDto;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.review.MovieReviewComment;
import com.filmdoms.community.account.data.entity.review.MovieReviewContent;
import com.filmdoms.community.account.data.entity.review.MovieReviewHeader;
import com.filmdoms.community.account.data.entity.review.MovieReviewTag;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.account.repository.MovieReviewCommentRepository;
import com.filmdoms.community.account.repository.MovieReviewHeaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieReviewService {

    private final MovieReviewHeaderRepository headerRepository;
    private final MovieReviewCommentRepository commentRepository;
    private final AccountRepository accountRepository;

    public List<MovieReviewMainPageDto> getMainPageDtos() {
        return headerRepository.findTop5ByOrderByDateCreatedDesc()
                .stream()
                .map(MovieReviewMainPageDto::new)
                .collect(Collectors.toList());
    }

    public void initData() throws InterruptedException {
        Account author = Account.of("user1", "1234", AccountRole.USER);
        accountRepository.save(author);

        for(int i = 0; i < 10; i++) {
            MovieReviewHeader header = MovieReviewHeader.builder()
                    .tag(MovieReviewTag.A)
                    .title("review " + i)
                    .author(author)
                    .content(new MovieReviewContent("test content"))
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
