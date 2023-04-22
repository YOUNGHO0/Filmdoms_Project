package com.filmdoms.community.account.data.dto.request;

import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_EMAIL;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_NICKNAME;
import static com.filmdoms.community.account.exception.ValidationMessage.UNMATCHED_PASSWORD;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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

    @Email(message = UNMATCHED_EMAIL)
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,100}", message = UNMATCHED_PASSWORD)
    private String password;

    @Min(value = 2, message = UNMATCHED_NICKNAME)
    @Max(value = 20, message = UNMATCHED_NICKNAME)
    private String nickname;
}
