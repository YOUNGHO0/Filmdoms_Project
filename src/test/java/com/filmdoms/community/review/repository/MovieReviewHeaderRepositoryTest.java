package com.filmdoms.community.review.repository;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.review.data.entity.MovieReviewContent;
import com.filmdoms.community.review.data.entity.MovieReviewHeader;
import com.filmdoms.community.review.data.constant.MovieReviewTag;
import com.filmdoms.community.review.repository.MovieReviewHeaderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class MovieReviewHeaderRepositoryTest {

    @Autowired
    MovieReviewHeaderRepository movieReviewHeaderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void 생성일_내림차순으로_상위_5개_반환() throws InterruptedException {

        Account author = Account.of("user1", "1234", AccountRole.USER);
        accountRepository.save(author);

        for(int i = 0; i < 10; i++) {
            MovieReviewHeader header = MovieReviewHeader.builder()
                    .tag(MovieReviewTag.A)
                    .title("review " + i)
                    .author(author)
                    .content(new MovieReviewContent("test content"))
                    .build();
            movieReviewHeaderRepository.save(header);
            Thread.sleep(10);
        }

        List<MovieReviewHeader> result = movieReviewHeaderRepository.findTop5ByOrderByDateCreatedDesc();
        for(int i = 0; i < 5; i++) {
            Assertions.assertThat(result.get(i).getTitle())
                    .endsWith(String.valueOf(9 - i));
        }
    }

}