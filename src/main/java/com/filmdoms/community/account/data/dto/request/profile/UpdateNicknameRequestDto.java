package com.filmdoms.community.account.data.dto.request.profile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_NICKNAME;

@Getter
public class UpdateNicknameRequestDto {
    @Min(value = 2, message = UNMATCHED_NICKNAME)
    @Max(value = 20, message = UNMATCHED_NICKNAME)
    private String newNickname;


}
