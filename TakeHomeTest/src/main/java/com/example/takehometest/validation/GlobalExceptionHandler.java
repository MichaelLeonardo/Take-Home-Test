package com.example.takehometest.validation;

import com.example.takehometest._enum.ResponseCodeEnum;
import com.example.takehometest.base.BaseErrorMessage;
import com.example.takehometest.base.BaseResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse<?>> handleValidationException(
            ValidationException ex
    ) {

        BaseResponse<Object> response = new BaseResponse<>();

        response.setStatusCodeByEnum(ResponseCodeEnum.BAD_REQUEST);

        response.getErrorMessages().add(
                BaseErrorMessage.builder()
                        .field("validation")
                        .message(ex.getMessage())
                        .build()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleGeneralException(
            Exception ex
    ) {

        log.error(ex.getMessage(), ex);

        BaseResponse<Object> response = new BaseResponse<>();

        response.setStatusCodeByEnum(ResponseCodeEnum.ERROR);

        response.getErrorMessages().add(
                BaseErrorMessage.builder()
                        .field("server")
                        .message("Internal server error")
                        .build()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
