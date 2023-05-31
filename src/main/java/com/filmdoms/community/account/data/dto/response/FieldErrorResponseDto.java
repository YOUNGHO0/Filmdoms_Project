package com.filmdoms.community.account.data.dto.response;

import lombok.Getter;

@Getter
public class FieldErrorResponseDto {

    String field;
    String message;

    private FieldErrorResponseDto(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public static FieldErrorResponseDto from(String field, String message) {
        return new FieldErrorResponseDto(field, message);
    }
}
