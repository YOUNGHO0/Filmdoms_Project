package com.filmdoms.community.board.notice.repository;

import com.filmdoms.community.account.data.constants.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.exception.ApplicationException;
import com.filmdoms.community.account.exception.ErrorCode;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.annotation.DataJpaTestWithJpaAuditing;
import com.filmdoms.community.board.comment.data.entity.Comment;
import com.filmdoms.community.board.comment.repository.CommentRepository;
import com.filmdoms.community.board.data.BoardContent;
import com.filmdoms.community.board.notice.data.entity.NoticeHeader;
import com.filmdoms.community.imagefile.data.entitiy.ImageFile;
import com.filmdoms.community.imagefile.repository.ImageFileRepository;
import com.filmdoms.community.imagefile.service.ImageFileService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTestWithJpaAuditing
class NoticeHeaderRepositoryTest {

    @Autowired
    NoticeHeaderRepository headerRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ImageFileRepository imageFileRepository;

    @MockBean
    ImageFileService imageFileService;

    @PersistenceContext
    EntityManager em;

    @Autowired
    AccountRepository accountRepository;

    static int userCount = 0;

    @Test //확인 용도로 작성한 테스트입니다. 이해 용도로 대강 만든 거라 나중에 에러 뱉으면 그냥 삭제하셔도 무방합니다.
    @DisplayName("쿼리 확인용 테스트")
    void repoTest() {
        //given
        NoticeHeader header = NoticeHeader.builder()
                .title("aa")
                .author(createAuthor())
                .boardContent(BoardContent.builder()
                        .content("aa")
                        .build())
                .build();
        headerRepository.save(header);

        createTestImage(header.getBoardContent());
        createTestImage(header.getBoardContent());
        createTestImage(header.getBoardContent()); //헤더에 이미지 3장 연결

        Comment c1 = Comment.builder()
                .header(header)
                .content("aa")
                .author(createAuthor())
                .build();
        Comment c2 = Comment.builder()
                .header(header)
                .content("aa")
                .author(createAuthor())
                .build(); //헤더에 댓글 2개 연결

        commentRepository.save(c1);
        commentRepository.save(c2);

        em.flush();
        em.clear(); //다시 불러오기

        //when
        System.out.println("쿼리 시작");
        NoticeHeader findHeader = headerRepository.findByIdWithAuthorBoardContentMainImageContentImages(header.getId()).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_POST_ID));
        List<Comment> comments = commentRepository.findByHeaderIdWithAuthor(header.getId());
        for (Comment comment : comments) {
            String username = comment.getAuthor().getUsername();
            System.out.println("username = " + username);
        }
        System.out.println("쿼리 끝"); //select 쿼리 2번 나가는 것 확인

        //then
        //이미지, 댓글 개수 문제 없는지 확인
        assertThat(findHeader.getBoardContent().getImageFiles().size()).isEqualTo(3);
        assertThat(comments).hasSize(2);
    }

    private ImageFile createTestImage(BoardContent content) {
        ImageFile testImageFile = ImageFile.builder()
                .originalFileName("testOriginalFileName")
                .uuidFileName("testUuidFileName")
                .boardContent(content)
                .build();
        return imageFileRepository.save(testImageFile);
    }

    private Account createAuthor() {
        userCount++;
        Account account = Account.builder()
                .username("user" + userCount)
                .password("1234")
                .role(AccountRole.USER)
                .build();
        return accountRepository.save(account);
    }
}