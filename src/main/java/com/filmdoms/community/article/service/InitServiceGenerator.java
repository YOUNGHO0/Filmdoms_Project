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
import com.filmdoms.community.comment.data.entity.Comment;
import com.filmdoms.community.comment.repository.CommentRepository;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class InitServiceGenerator {

    private final FileRepository fileRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;
    private final FilmUniverseRepository filmUniverseRepository;
    private final AnnounceRepository announceRepository;
    private final ArticleRepository articleRepository;
    private final CriticRepository criticRepository;

    public File fileGenerator(String uuidFileName, String originalFileName) {
        File fileImage = File.builder() //게시글과 매핑될 디폴트 이미지 생성
                .uuidFileName(uuidFileName)
                .originalFileName(originalFileName)
                .build();
        fileRepository.save(fileImage);
        return fileImage;
    }

    public Account accountGenerator(String nickname, AccountRole role, File progfileImage) {
        Account user = Account.builder() //게시글, 댓글과 매핑될 Account 생성
                .nickname(nickname)
                .role(role)
                .build();
        accountRepository.save(user);
        return user;
    }

    public Comment commentGenerator(Article article, Account account, String content) {
        Comment comment = Comment.builder() //댓글 생성
                .article(article)
                .author(account)
                .content(content)
                .build();
        commentRepository.save(comment);
        return comment;
    }

    public Comment childCommentGenerator(Article article, Account account, Comment parentComment, String content) {
        Comment childComment = Comment.builder() //댓글 생성
                .article(article)
                .author(account)
                .parentComment(parentComment)
                .content(content)
                .build();
        commentRepository.save(childComment);
        return childComment;
    }

    public Article articleGenerator(String title, Category category, Tag tag, Account account, boolean containsImage, String content) {
        Article article = Article.builder()
                .title(title)
                .category(category)
                .tag(tag)
                .author(account)
                .containsImage(containsImage)
                .content(content)
                .build();
        articleRepository.save(article);
        return article;

    }

    public FilmUniverse filmUniverseGenerator(File file, Article article, LocalDateTime startTime, LocalDateTime endTime) {
        FilmUniverse filmUniverse = FilmUniverse.builder()
                .mainImage(file)
                .article(article)
                .startDate(startTime)
                .endDate(endTime)
                .build();
        filmUniverseRepository.save(filmUniverse);
        return filmUniverse;
    }

    public Critic criticGenerator(Article article, File mainImage) {

        Critic critic = Critic.builder()
                .article(article)
                .mainImage(mainImage)
                .build();
        criticRepository.save(critic);
        return critic;

    }

    public Announce announceGenerator(Article article) {
        Announce announce = Announce.builder()
                .article(article)
                .build();
        announceRepository.save(announce);
        return announce;
    }


}
