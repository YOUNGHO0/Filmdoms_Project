package com.filmdoms.community.exception;

import com.filmdoms.community.account.data.dto.response.FieldErrorResponseDto;
import com.filmdoms.community.account.data.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationHandler(ApplicationException e) {
        log.error("Error occurs: {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error occurs: {}", e.toString());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> JsonParseExceptionHandler(
            HttpMessageNotReadableException e) {
        log.error("Error occurs: {}", e.toString());
        return ResponseEntity.status(ErrorCode.REQUEST_PARSE_ERROR.getStatus())
                .body(Response.error(ErrorCode.REQUEST_PARSE_ERROR.name()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> validExceptionHandler(
            MethodArgumentNotValidException e) {

        // 파싱 중 Validation에 걸린 필드의 예외 메시지를 모두 모읍니다.
        List<FieldErrorResponseDto> errorResponseDtos = e.getFieldErrors()
                .stream()
                .map(fieldError -> FieldErrorResponseDto.from(fieldError.getField(), resolveFieldErrorMessage(fieldError)))
                .collect(Collectors.toList());

        return ResponseEntity.status(ErrorCode.REQUEST_PARSE_ERROR.getStatus())
                .body(Response.error(ErrorCode.REQUEST_PARSE_ERROR.name(), errorResponseDtos));
    }

    private String resolveFieldErrorMessage(FieldError error) {
        Object[] arguments = error.getArguments();
        Locale locale = LocaleContextHolder.getLocale();

        return Arrays.stream(error.getCodes())
                .map(c -> {
                    try {
                        return messageSource.getMessage(c, arguments, locale);
                    } catch (NoSuchMessageException e) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .findFirst()
                // 코드를 찾지 못할 경우 기본 메시지 사용
                .orElse(error.getDefaultMessage());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<?> cookieExceptionHandler(
            MissingRequestCookieException e) {
        log.error("Error occurs: {}", e.toString());
        return ResponseEntity.status(ErrorCode.TOKEN_NOT_FOUND.getStatus())
                .body(Response.error(ErrorCode.TOKEN_NOT_FOUND.name(), ErrorCode.TOKEN_NOT_FOUND.getMessage()));
    }
}

