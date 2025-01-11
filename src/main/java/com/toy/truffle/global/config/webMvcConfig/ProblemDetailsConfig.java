package com.toy.truffle.global.config.webMvcConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class ProblemDetailsConfig implements WebMvcConfigurer {

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		// 커스텀 Validation Exception Resolver 등록
		resolvers.add(0, new CustomValidationExceptionResolver());
	}

	private static class CustomValidationExceptionResolver implements HandlerExceptionResolver {

		@Override
		public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
			// MethodArgumentNotValidException 처리
			if (ex instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
				handleValidationException((org.springframework.web.bind.MethodArgumentNotValidException) ex, response, request);
				return new ModelAndView();
			}
			// 다른 예외는 Spring 기본 처리기로 전달
			return null;
		}

		private void handleValidationException(org.springframework.web.bind.MethodArgumentNotValidException ex,
			HttpServletResponse response,
			HttpServletRequest request) {
			// 유효성 검증 에러 메시지를 필드-메시지 형태로 수집
			Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(
					fieldError -> fieldError.getField(),
					fieldError -> fieldError.getDefaultMessage(),
					(existing, replacement) -> existing // 중복 키 발생 시 기존 값 유지
				));

			// JSON 응답 생성 및 반환
			try {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				response.setContentType("application/problem+json");
				response.getWriter().write(String.format("""
                    {
                        "type": "https://example.com/errors/validation-error",
                        "title": "Validation Error",
                        "status": 400,
                        "detail": %s,
                        "instance": "%s"
                    }
                    """, new ObjectMapper().writeValueAsString(errors), request.getRequestURI()));
			} catch (IOException e) {
				log.error("handleValidationException 에러 발생 : {}", e.getMessage(), e);
			}
		}
	}
}
