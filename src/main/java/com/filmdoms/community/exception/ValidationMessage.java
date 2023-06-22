package com.filmdoms.community.exception;

public class ValidationMessage {

    public static final String CANNOT_BE_NULL = "필수로 입력되어야 합니다.";
    public static final String TITLE_NOT_BLANK = "제목은 공백일 수 없습니다.";
    public static final String TITLE_SIZE = "제목은 100자 이내이어야 합니다.";
    public static final String CONTENT_NOT_BLANK = "본문은 공백일 수 없습니다.";
    public static final String CONTENT_SIZE = "본문은 10000자 이내이어야 합니다.";
    public static final String IMAGE_REQUIRED = "이미지는 필수로 첨부해주어야 합니다.";
    //    public static final String UNMATCHED_USERNAME = "아이디는 8자 이상, 20자 이하의 소문자, 숫자 혹은 \"_\" 의 조합이어야 합니다.";
    public static final String UNMATCHED_PASSWORD = "비밀번호는 8자 이상, 100자 이하의 대문자, 소문자, 숫자, 및 특수문자의 조합이어야 합니다.";
    public static final String UNMATCHED_EMAIL = "형식에 맞는 이메일 주소여야 합니다.";
    public static final String UNMATCHED_NICKNAME = "닉네임은 2자 이상, 20자 이하이어야 합니다.";
    public static final String LIST_TOO_BIG = "개수가 너무 많습니다.";
    public static final String EMAIL_AUTH_ERROR = "이메일 인증을 해 주세요";
}
