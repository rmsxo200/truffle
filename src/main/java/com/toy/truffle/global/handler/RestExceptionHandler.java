package com.toy.truffle.global.handler;

import com.toy.truffle.global.config.problemDetails.CustomException;
import com.toy.truffle.global.config.problemDetails.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ProblemDetail handleRestCustomException(CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), errorCode.getDetail());
        problemDetail.setTitle(errorCode.getTitle());
        // FIXME : 스웨거 추가시 URI 수정
        problemDetail.setType(URI.create("https://example.com/errors/" + errorCode.getCode()));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }
}