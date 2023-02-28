package com.filmdoms.community.board.banner.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.IMAGE_REQUIRED;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_NOT_BLANK;
import static com.filmdoms.community.account.exception.ValidationMessage.TITLE_SIZE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class BannerInfoRequestDto {

    @NotBlank(message = TITLE_NOT_BLANK)
    @Size(max = 100, message = TITLE_SIZE)
    private String title;

    @NotNull(message = IMAGE_REQUIRED)
    private Long mainImageId;
}
