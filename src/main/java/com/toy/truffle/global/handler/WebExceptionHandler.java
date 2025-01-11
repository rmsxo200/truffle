package com.toy.truffle.global.handler;

import com.toy.truffle.global.config.problemDetails.CustomException;
import com.toy.truffle.global.config.problemDetails.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleWebCustomException(CustomException ex, Model model, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        // 에러 메시지를 모델에 추가
        model.addAttribute("errorTitle", errorCode.getTitle());
        model.addAttribute("errorMessage", errorCode.getDetail());
        model.addAttribute("errorCode", errorCode.getCode());

        // 현재 요청된 URI로 다시 포워딩 (현재 페이지로 돌아감)
        return request.getRequestURI();
    }
}