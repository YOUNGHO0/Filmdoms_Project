package com.filmdoms.community.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러 발생."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "해당 유저는 요청을 수행할 권한이 없습니다."),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "인증 중 오류가 발생했습니다."),
    AUTHORIZATION_ERROR(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    URI_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 URI가 존재하지 않습니다."),
    REQUEST_PARSE_ERROR(HttpStatus.BAD_REQUEST, "요청 파싱 중 오류가 발생했습니다."),
    NO_MAIN_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "메인 이미지가 전송되지 않았습니다."),
    S3_ERROR(HttpStatus.FORBIDDEN, "S3 업로드에 실패했습니다."),
    INVALID_IMAGE_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 이미지 아이디입니다."),
    NO_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "이미지가 전달되지 않았습니다."),
    EMPTY_FILE_ERROR(HttpStatus.BAD_REQUEST, "비어 있는 파일입니다."),
    IMAGE_BELONG_TO_OTHER_POST(HttpStatus.BAD_REQUEST, "다른 게시물에 포함된 이미지입니다."),
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물 아이디입니다."),
    MAIN_IMAGE_ID_NOT_IN_CONTENT_IMAGE_ID_LIST(HttpStatus.BAD_REQUEST, "메인 이미지 ID는 전체 이미지 ID 리스트에 포함되어야 합니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
