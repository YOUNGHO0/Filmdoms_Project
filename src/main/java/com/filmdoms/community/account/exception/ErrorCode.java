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
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "헤더에 토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    TOKEN_NOT_IN_DB(HttpStatus.NOT_FOUND, "저장된 리프레시 토큰을 찾지 못했습니다. 만료되었거나, 액세스 토큰을 잘못 보내지 않았는지 확인해주세요."),
    URI_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 URI가 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 주소의 게시판이 존재하지 않습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 주소의 태그가 존재하지 않습니다."),
    REQUEST_PARSE_ERROR(HttpStatus.BAD_REQUEST, "요청 파싱 중 오류가 발생했습니다."),
    NO_MAIN_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "메인 이미지가 전송되지 않았습니다."),
    S3_ERROR(HttpStatus.FORBIDDEN, "S3 업로드에 실패했습니다."),
    INVALID_IMAGE_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 이미지 아이디입니다."),
    NO_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "이미지가 전달되지 않았습니다."),
    EMPTY_FILE_ERROR(HttpStatus.BAD_REQUEST, "비어 있는 파일입니다."),
    IMAGE_BELONG_TO_OTHER_POST(HttpStatus.BAD_REQUEST, "다른 게시물에 포함된 이미지입니다."),
    INVALID_POST_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물 아이디입니다."), //삭제 예정
    INVALID_ARTICLE_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 게시물 아이디입니다."),
    INVALID_PAGE_NUMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 페이지 입니다."),
    INVALID_COMMENT_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 코멘트 아이디입니다."),
    COMMENT_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "비활성화되었거나 삭제된 코멘트입니다."),
    INVALID_PARENT_COMMENT_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 부모 댓글 아이디이거나, 게시물에 해당 부모 댓글이 존재하지 않습니다."),
    MANAGER_COMMENT_CANNOT_BE_CREATED(HttpStatus.BAD_REQUEST, "관리자 댓글을 생성할 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 회원 닉네임 입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 회원 이메일 입니다."),
    NO_KEYWORD_FOUND(HttpStatus.BAD_REQUEST, "검색할 키워드가 누락되었습니다"),
    TOO_MANY_KEYWORD(HttpStatus.BAD_REQUEST, "한개의 키워드로만 검색 가능합니다"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    MAIN_IMAGE_ID_NOT_IN_CONTENT_IMAGE_ID_LIST(HttpStatus.BAD_REQUEST, "메인 이미지 ID는 전체 이미지 ID 리스트에 포함되어야 합니다."),
    NOT_SOCIAL_LOGIN_ACCOUNT(HttpStatus.BAD_REQUEST, "소셜 로그인 계정이 아닙니다."),
    SOCIAL_LOGIN_ACCOUNT(HttpStatus.BAD_REQUEST, "소셜 로그인 계정입니다."),
    INVALID_AUTHENTICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드이거나 인증 코드가 만료되었습니다.");

    private final HttpStatus status;
    private final String message;
}
