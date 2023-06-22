package com.filmdoms.community.account.data.dto.request;

import static com.filmdoms.community.exception.ValidationMessage.UNMATCHED_PASSWORD;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdatePasswordRequestDto {
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,100}", message = UNMATCHED_PASSWORD)
    private String newPassword;
}
