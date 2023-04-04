package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import lombok.Getter;

/**
 * 게시글 상세 페이지에서 글 작성자, 댓글 작성자의 정보를 응답하기 위한 DTO
 */
@Getter
public class DetailPageAccountResponseDto {

    private Long id;
    private String username;
    private FileResponseDto profileImage;

    private DetailPageAccountResponseDto(Long id, String username, FileResponseDto profileImage) {
        this.id = id;
        this.username = username;
        this.profileImage = profileImage;
    }

    public static DetailPageAccountResponseDto from(Account account) {
        return new DetailPageAccountResponseDto(
                account.getId(),
                account.getUsername(),
                FileResponseDto.from(account.getProfileImage())
        );
    }
}

