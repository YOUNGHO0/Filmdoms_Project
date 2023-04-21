package com.filmdoms.community.banner.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.IMAGE_REQUIRED;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_NOT_BLANK;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_SIZE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class BannerRequestDto {

    @NotBlank(message = TITLE_NOT_BLANK)
    @Size(max = 100, message = TITLE_SIZE)
    private String title;

    @NotNull(message = IMAGE_REQUIRED)
    private Long fileId;
}
