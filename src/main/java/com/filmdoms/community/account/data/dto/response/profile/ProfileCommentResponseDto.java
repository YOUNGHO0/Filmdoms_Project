package com.filmdoms.community.account.data.dto.response.profile;

import com.filmdoms.community.comment.data.entity.Comment;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProfileCommentResponseDto {

    private List<ProfileSingleCommentResponseDto> comments;
    private SimplePageInfoResponseDto pageInfo;

    private ProfileCommentResponseDto(Page<Comment> commentPage) {
        this.comments = commentPage.getContent()
                .stream()
                .map(ProfileSingleCommentResponseDto::from)
                .toList();
        this.pageInfo = SimplePageInfoResponseDto.from(commentPage);
    }

    public static ProfileCommentResponseDto from(Page<Comment> commentPage) {
        return new ProfileCommentResponseDto(commentPage);
    }
}
