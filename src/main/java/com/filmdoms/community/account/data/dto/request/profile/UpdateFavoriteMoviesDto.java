package com.filmdoms.community.account.data.dto.request.profile;

import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

import static com.filmdoms.community.account.exception.ValidationMessage.LIST_TOO_BIG;

@Getter
public class UpdateFavoriteMoviesDto {

    @Size(max = 5, message = "좋아하는 영화 " + LIST_TOO_BIG)
    private List<String> favoriteMovies;
}
