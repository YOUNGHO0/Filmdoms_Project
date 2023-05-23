package com.filmdoms.community.article.service;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.repository.AccountRepository;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.article.data.entity.extra.Announce;
import com.filmdoms.community.article.data.entity.extra.Critic;
import com.filmdoms.community.article.data.entity.extra.FilmUniverse;
import com.filmdoms.community.article.repository.AnnounceRepository;
import com.filmdoms.community.article.repository.ArticleRepository;
import com.filmdoms.community.article.repository.CriticRepository;
import com.filmdoms.community.article.repository.FilmUniverseRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.data.entity.FileContent;
import com.filmdoms.community.file.repository.FileContentRepository;
import com.filmdoms.community.file.repository.FileRepository;
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final AccountRepository accountRepository;
    private final ArticleRepository articleRepository;
    private final FilmUniverseRepository filmUniverseRepository;
    private final CriticRepository criticRepository;
    private final FileRepository fileRepository;
    private final FileContentRepository fileContentRepository;
    private final CommentRepository commentRepository;
    private final AnnounceRepository announceRepository;

    public void makeArticleData(int limit) {

        File defaultImage = File.builder() //게시글과 매핑될 디폴트 이미지 생성
                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                .originalFileName("original_file_name")
                .build();
        fileRepository.save(defaultImage);

        File defaultImage2 = File.builder() //게시글과 매핑될 디폴트 이미지 생성
                .uuidFileName("7f5fb6d2-40fa-4e3d-81e6-a013af6f4f23.png")
                .originalFileName("original_file_name")
                .build();
        fileRepository.save(defaultImage2);

        Account admin = Account.builder() //게시글, 댓글과 매핑될 Account 생성
                .email("admin@filmdoms.com")
                .nickname("ironman")
                .role(AccountRole.ADMIN)
                .profileImage(defaultImage) //프로필 이미지를 디폴트 이미지로 세팅
                .build();
        accountRepository.save(admin);

        List<Tag> movieTagList = Arrays.stream(Tag.values()) //영화 게시판 태그만 추출
                .filter(tag -> tag.getCategory() == Category.MOVIE)
                .toList();
        for (int i = 1; i <= limit; i++) {
            Article article = Article.builder() //게시글 생성
                    .title("Movie 게시판 " + i + "번째 글 제목")
                    .category(Category.MOVIE)
                    .tag(movieTagList.get(i % movieTagList.size()))
                    .author(admin)
                    .content("Movie 게시판 " + i + "번째 글 내용\nMovie 게시판 " + i + "번째 글 내용")
                    .containsImage(false)
                    .build();
            for(int p=i; p<=limit-3; p++)
                article.addVote();
            articleRepository.save(article);

//            FileContent fileContent = FileContent.builder()
//                    .file(defaultImage)
//                    .content(article.getContent())
//                    .build();
//
//            FileContent fileContent2 = FileContent.builder()
//                    .file(defaultImage2)
//                    .content(article.getContent())
//                    .build();
//
//            fileContentRepository.save(fileContent);
//            fileContentRepository.save(fileContent2);

            int commentNum = (i - 1) % 3 + 1; //댓글 개수는 1,2,3,1,2,3,... 순서로 데이터 만들기
            for (int j = 1; j <= commentNum; j++) {
                Comment comment = Comment.builder() //댓글 생성
                        .article(article)
                        .author(admin)
                        .content("Movie 게시판 " + i + "번째 글의 " + j + "번째 댓글 내용")
                        .build();
                for (int vc = 0; vc < 5; vc++) {
                    comment.addVote();
                }
                commentRepository.save(comment);

                for (int k = 1; k < 3; k++) {
                    Comment childComment = Comment.builder() //댓글 생성
                            .article(article)
                            .author(admin)
                            .parentComment(comment)
                            .content("Movie 게시판 " + i + "번째 글의 " + j + "번째 댓글의 " + k + "번째 대댓글 내용")
                            .build();
                    for (int vc = 0; vc < 5; vc++) {
                        childComment.addVote();
                    }
                    commentRepository.save(childComment);
                }
            }
        }

        List<Tag> noticeTagList = Arrays.stream(Tag.values())
                .filter(tag -> tag.getCategory() == Category.FILM_UNIVERSE)
                .toList();
        for (int i = 1; i <= limit; i++) {
            Article article = Article.builder()
                    .title("File Universe 게시판 " + i + "번째 글 제목")
                    .category(Category.FILM_UNIVERSE)
                    .tag(noticeTagList.get(i % noticeTagList.size()))
                    .author(admin)
                    .containsImage(true)
                    .content("File Universe 게시판 " + i + "번째 글 내용\nFile Universe 게시판 " + i + "번째 글 내용")
                    .build();
            for(int p=i/3; p<=limit/2; p++)
                article.addVote();

            FilmUniverse filmUniverse = FilmUniverse.builder()
                    .mainImage(defaultImage)
                    .article(article)
                    .startDate(LocalDateTime.of(2023, 4, 1, 0, 0, 0).plusDays(i - 1))
                    .endDate(LocalDateTime.of(2023, 5, 1, 0, 0, 0).plusDays(i - 1))
                    .build();
            filmUniverseRepository.save(filmUniverse);

            FileContent fileContent = FileContent.builder()
                    .file(defaultImage)
                    .content(article.getContent())
                    .build();

            FileContent fileContent2 = FileContent.builder()
                    .file(defaultImage2)
                    .content(article.getContent())
                    .build();

            fileContentRepository.save(fileContent);
            fileContentRepository.save(fileContent2);

            int commentNum = (i - 1) % 3 + 1;
            for (int j = 1; j <= commentNum; j++) {
                Comment comment = Comment.builder()
                        .article(article)
                        .author(admin)
                        .content("File Universe 게시판 " + i + "번째 글의 " + j + "번째 댓글 내용")
                        .build();
                for (int vc = 0; vc < 5; vc++) {
                    comment.addVote();
                }
                commentRepository.save(comment);

                for (int k = 1; k < 3; k++) {
                    Comment childComment = Comment.builder() //댓글 생성
                            .article(article)
                            .author(admin)
                            .parentComment(comment)
                            .content("File Universe 게시판 " + i + "번째 글의 " + j + "번째 댓글의 " + k + "번째 대댓글 내용")
                            .build();
                    for (int vc = 0; vc < 5; vc++) {
                        childComment.addVote();
                    }
                    commentRepository.save(childComment);
                }
            }
        }

        List<Tag> criticTagList = Arrays.stream(Tag.values())
                .filter(tag -> tag.getCategory() == Category.CRITIC)
                .toList();
        for (int i = 1; i <= limit; i++) {
            Article article = Article.builder()
                    .title("Critic 게시판 " + i + "번째 글 제목")
                    .category(Category.CRITIC)
                    .tag(criticTagList.get(i % criticTagList.size()))
                    .author(admin)
                    .containsImage(true)
                    .content("Critic 게시판 " + i + "번째 글 내용\nCritic 게시판 " + i + "번째 글 내용")
                    .build();
            for(int p=i; p<=limit-10; p++)
                article.addVote();
            Critic critic = Critic.builder()
                    .article(article)
                    .mainImage(defaultImage)
                    .build();
            criticRepository.save(critic);

            FileContent fileContent = FileContent.builder()
                    .file(defaultImage)
                    .content(article.getContent())
                    .build();

            FileContent fileContent2 = FileContent.builder()
                    .file(defaultImage2)
                    .content(article.getContent())
                    .build();

            fileContentRepository.save(fileContent);
            fileContentRepository.save(fileContent2);

            int commentNum = (i - 1) % 3 + 1;
            for (int j = 1; j <= commentNum; j++) {
                Comment comment = Comment.builder()
                        .article(article)
                        .author(admin)
                        .content("Critic 게시판 " + i + "번째 글의 " + j + "번째 댓글 내용")
                        .build();
                for (int vc = 0; vc < 5; vc++) {
                    comment.addVote();
                }
                commentRepository.save(comment);

                for (int k = 1; k < 3; k++) {
                    Comment childComment = Comment.builder() //댓글 생성
                            .article(article)
                            .author(admin)
                            .parentComment(comment)
                            .content("Critic 게시판 " + i + "번째 글의 " + j + "번째 댓글의 " + k + "번째 대댓글 내용")
                            .build();
                    for (int vc = 0; vc < 5; vc++) {
                        childComment.addVote();
                    }
                    commentRepository.save(childComment);
                }
            }
        }

        for(int i=0; i<limit/2; i++)
        {
            Article article = Article.builder()
                    .title("공지사항 게시판 " + i + "번째 글 제목")
                    .category(Category.FILM_UNIVERSE)
                    .tag(noticeTagList.get(i % noticeTagList.size()))
                    .author(admin)
                    .containsImage(false)
                    .content("공지사항 게시판 " + i + "번째 글 내용\n 공지사항 게시판 " + i + "번째 글 내용")
                    .build();
            articleRepository.save(article);
            Announce announce = Announce.builder()
                    .article(article)
                    .build();
            announceRepository.save(announce);

        }

        for(int i=limit/2; i<limit; i++)
        {
            Article article = Article.builder()
                    .title("공지사항 게시판 " + i + "번째 글 제목")
                    .category(Category.CRITIC)
                    .tag(criticTagList.get(i % noticeTagList.size()))
                    .author(admin)
                    .containsImage(false)
                    .content("공지사항 게시판 " + i + "번째 글 내용\n 공지사항 게시판 " + i + "번째 글 내용")
                    .build();
            articleRepository.save(article);
            Announce announce = Announce.builder()
                    .article(article)
                    .build();
            announceRepository.save(announce);
        }
    }
}
