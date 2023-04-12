package com.filmdoms.community.account.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.IMAGE_REQUIRED;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_NICKNAME;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateProfileRequestDto {

    @NotNull(message = IMAGE_REQUIRED)
    private Long fileId;

    @Min(value = 2, message = UNMATCHED_NICKNAME)
    @Max(value = 20, message = UNMATCHED_NICKNAME)
    private String nickname;
}
