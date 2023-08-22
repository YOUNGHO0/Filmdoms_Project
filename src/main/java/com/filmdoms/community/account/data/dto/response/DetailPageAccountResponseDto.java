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
    private String nickname;
    private FileResponseDto profileImage;

    private DetailPageAccountResponseDto(Long id, String nickname, FileResponseDto profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static DetailPageAccountResponseDto from(Account account) {
        return new DetailPageAccountResponseDto(
                account.getId(),
                account.getNickname(),
                FileResponseDto.from(account.getProfileImage())
        );
    }

    public static DetailPageAccountResponseDto makeMockAccount() {
        return new DetailPageAccountResponseDto(-1L, "사용자", FileResponseDto.from("8ffbe57a-741e-46a2-bbda-f507ed6c51fb.png"));
    }

}

