package com.filmdoms.community.account.data.dto.response;

import com.filmdoms.community.account.data.constant.AccountRole;
import com.filmdoms.community.account.data.constant.AccountStatus;
import com.filmdoms.community.account.data.entity.Account;
import com.filmdoms.community.account.data.entity.Movie;
import com.filmdoms.community.account.data.entity.FavoriteMovie;
import com.filmdoms.community.file.data.dto.response.FileResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountResponseDto {

    private Long id;
    private String nickname;
    private String email;
    private AccountRole accountRole;
    private AccountStatus accountStatus;
    private boolean isSocialLogin;
    private FileResponseDto profileImage;

    private List<String> favoriteMovies;

    private AccountResponseDto(Account account, List<FavoriteMovie> favoriteMovies) {
        this.id = account.getId();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
        this.accountRole = account.getAccountRole();
        this.accountStatus = account.getAccountStatus();
        this.isSocialLogin = account.isSocialLogin();
        this.profileImage = FileResponseDto.from(account.getProfileImage());
        this.favoriteMovies = favoriteMovies.stream()
                .map(FavoriteMovie::getMovie)
                .map(Movie::getName)
                .collect(Collectors.toList());
    }

    public static AccountResponseDto from(Account account, List<FavoriteMovie> favoriteMovies) {
        return new AccountResponseDto(account, favoriteMovies);
    }
}
