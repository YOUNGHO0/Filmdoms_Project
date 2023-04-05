package com.filmdoms.community.article.data.dto.response.detail;

import com.filmdoms.community.account.data.dto.response.DetailPageAccountResponseDto;
import com.filmdoms.community.article.data.constant.Category;
import com.filmdoms.community.article.data.constant.Tag;
import com.filmdoms.community.article.data.entity.Article;
import com.filmdoms.community.board.data.constant.PostStatus;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import com.filmdoms.community.file.data.entity.File;
import com.filmdoms.community.newcomment.data.dto.response.ParentNewCommentResponseDto;
import com.filmdoms.community.newcomment.data.entity.NewComment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 영화, 비평 게시판의 게시글 상세 페이지 응답 DTO
 */
@Getter
public class ArticleDetailResponseDto {

    private Long id;
    private Category category;
    private Tag tag;
    private String title;
    private PostStatus status;
    private int view;
    private int vote_count;
    private int commentCount;
    private boolean isVoted;
    private String content;
    private LocalDateTime dateCreated;
    private LocalDateTime dateLastModified;
    private DetailPageAccountResponseDto author;
    private List<FileResponseDto> images;
    private List<ParentNewCommentResponseDto> comments;

    protected ArticleDetailResponseDto(Article article, List<File> images, List<NewComment> comments, boolean isVoted) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.status = article.getStatus();
        this.view = article.getView();
        this.vote_count = article.getVoteCount();
        this.commentCount = comments.size();
        this.isVoted = isVoted;
        this.content = article.getContent().getContent();
        this.dateCreated = article.getDateCreated();
        this.dateLastModified = article.getDateLastModified();
        this.author = DetailPageAccountResponseDto.from(article.getAuthor());
        this.images = images.stream().sorted(Comparator.comparing(File::getId)).map(FileResponseDto::from).toList(); //id로 정렬한 뒤 DTO 변환
        this.comments = ParentNewCommentResponseDto.convert(comments);
    }

    public static ArticleDetailResponseDto from(Article article, List<File> images, List<NewComment> comments, boolean isVoted) {
        return new ArticleDetailResponseDto(article, images, comments, isVoted);
    }
}
