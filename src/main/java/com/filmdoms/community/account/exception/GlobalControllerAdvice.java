package com.filmdoms.community.account.exception;

import com.filmdoms.community.account.data.dto.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

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
        log.error("Error occurs: {}", ErrorCode.REQUEST_PARSE_ERROR.getMessage());
        // 파싱 중 Validation에 걸린 필드의 예외 메시지를 모두 모읍니다.
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(ErrorCode.REQUEST_PARSE_ERROR.getStatus())
                .body(Response.error(ErrorCode.REQUEST_PARSE_ERROR.name(), errors));
    }
}

