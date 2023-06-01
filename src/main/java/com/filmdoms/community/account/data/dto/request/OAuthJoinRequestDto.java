package com.filmdoms.community.account.data.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OAuthJoinRequestDto {

    @NotNull
    @Size(min = 2, max = 20)
    private String nickname;

    @NotNull
    @Size(min = 1, max = 5)
    private List<String> favoriteMovies;
}

