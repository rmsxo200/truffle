package com.toy.truffle.unit.global.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.toy.truffle.global.config.problemDetails.CustomException;
import com.toy.truffle.global.config.problemDetails.ErrorCode;
import com.toy.truffle.global.handler.RestExceptionHandler;
import com.toy.truffle.global.handler.WebExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

@ActiveProfiles("test")
class ExceptionHandlerTest {

	@Test
	@DisplayName("Rest CustomException 에러 테스트")
	public void testRestControllerAdvice() {
		// Given
		RestExceptionHandler exceptionHandler = new RestExceptionHandler();
		CustomException customException = new CustomException(ErrorCode.DATA_SAVE_ERROR);
		// Mock HttpServletRequest 객체 생성 및 설정
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/data");  // URI 반환 설정
		// When
		ProblemDetail result = exceptionHandler.handleRestCustomException(customException, request);
		// Then
		assertThat(result.getTitle()).isEqualTo(ErrorCode.DATA_SAVE_ERROR.getTitle());
		assertThat(result.getDetail()).isEqualTo(ErrorCode.DATA_SAVE_ERROR.getDetail());
		assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(result.getInstance().toString()).isEqualTo("/data");
	}

	@Test
	@DisplayName("기본 CustomException 에러 테스트")
	public void testControllerAdvice() {
		// Given
		WebExceptionHandler exceptionHandler = new WebExceptionHandler();
		CustomException customException = new CustomException(ErrorCode.DATA_SAVE_ERROR);
		// 실제 모델 객체 사용
		Model model = new BindingAwareModelMap();
		// Mock HttpServletRequest 객체 생성 및 설정
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/data");
		// When
		String result = exceptionHandler.handleWebCustomException(customException, model, request);
		// Then
		assertThat(result).isEqualTo("/data"); // 현재 주소를 다시 반환 하는지 확인
		assertThat(model.getAttribute("errorTitle")).isEqualTo(
			ErrorCode.DATA_SAVE_ERROR.getTitle());  // 모델의 상태 검증
		assertThat(model.getAttribute("errorMessage")).isEqualTo(
			ErrorCode.DATA_SAVE_ERROR.getDetail());
		assertThat(model.getAttribute("errorCode")).isEqualTo(ErrorCode.DATA_SAVE_ERROR.getCode());
	}
}