package com.filmdoms.community.account.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.LIST_TOO_BIG;
import static com.filmdoms.community.account.exception.ValidationMessage.CANNOT_BE_NULL;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_EMAIL;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_NICKNAME;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_PASSWORD;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class JoinRequestDto {

//    @Pattern(regexp = "/^[a-z0-9_]{8,20}$/", message = UNMATCHED_USERNAME)
//    private String username;

    @NotNull(message = "이메일은 " + CANNOT_BE_NULL)
    @Email(message = UNMATCHED_EMAIL)
    private String email;

    @NotNull(message = "비밀번호는 " + CANNOT_BE_NULL)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,100}", message = UNMATCHED_PASSWORD)
    private String password;

    @NotNull(message = "닉네임은 " + CANNOT_BE_NULL)
    @Min(value = 2, message = UNMATCHED_NICKNAME)
    @Max(value = 20, message = UNMATCHED_NICKNAME)
    private String nickname;

    @NotNull(message = "관심 영화는 " + CANNOT_BE_NULL)
    @Size(max = 5, message = "관심 영화 " + LIST_TOO_BIG)
    private List<String> favoriteMovies;
}
