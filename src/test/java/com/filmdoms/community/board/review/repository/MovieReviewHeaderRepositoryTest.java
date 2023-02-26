package com.filmdoms.community.board.review.repository;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJPAAuditing;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.data.constant.MovieReviewTag;
import com.filmdoms.community.board.review.data.entity.MovieReviewHeader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTestWithJPAAuditing
class MovieReviewHeaderRepositoryTest {

    @Autowired
    MovieReviewHeaderRepository movieReviewHeaderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void 생성일_내림차순으로_상위_5개_반환() throws InterruptedException {

        //given
        Account author = Account.of("user1", "1234", AccountRole.USER);
        accountRepository.save(author);

        for (int i = 0; i < 10; i++) {
            MovieReviewHeader header = MovieReviewHeader.builder()
                    .tag(MovieReviewTag.A)
                    .title("review " + i)
                    .author(author)
                    .boardContent(BoardContent.builder().content("test content").build())
                    .build();
            movieReviewHeaderRepository.save(header);
            Thread.sleep(10);
        }

        //when
        List<MovieReviewHeader> result = movieReviewHeaderRepository.findTop5ByOrderByDateCreatedDesc();

        //then
        for (int i = 0; i < 5; i++) {
            Assertions.assertThat(result.get(i).getTitle())
                    .endsWith(String.valueOf(9 - i));
        }
    }
}