package com.filmdoms.community.account.data.dto.request;

import static com.filmdoms.community.exception.ValidationMessage.IMAGE_REQUIRED;
import static com.filmdoms.community.exception.ValidationMessage.LIST_TOO_BIG;
import static com.filmdoms.community.exception.ValidationMessage.UNMATCHED_NICKNAME;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
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

    @Size(max = 5, message = "좋아하는 영화 " + LIST_TOO_BIG)
    private List<String> favoriteMovies;
}
