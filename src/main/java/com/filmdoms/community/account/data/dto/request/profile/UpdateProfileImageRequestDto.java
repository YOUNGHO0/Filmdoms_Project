package com.filmdoms.community.account.data.dto.request.profile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static com.filmdoms.community.exception.ValidationMessage.IMAGE_REQUIRED;

@Getter
public class UpdateProfileImageRequestDto {

    @NotNull(message = IMAGE_REQUIRED)
    private Long imageId;

}
