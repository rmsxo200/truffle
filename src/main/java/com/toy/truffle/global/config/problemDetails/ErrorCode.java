package com.toy.truffle.global.config.problemDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // CRUD 관련 에러
    DATA_SAVE_ERROR("ERR1001", "데이터 저장 에러", "데이터를 저장할 수 없습니다.", HttpStatus.BAD_REQUEST),
    DATA_READ_ERROR("ERR1002", "데이터 조회 에러", "데이터를 조회할 수 없습니다.", HttpStatus.BAD_REQUEST),
    // 사용자 관련 에러
    USER_NOT_FOUND_ERROR("ERR1011", "사용자 조회 실패", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 데이터 Validation 에러
    DEFAULT_VALIDATION_ERROR("ERR2001", "입력 데이터 에러", "입력한 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_ERROR("ERR2002", "사용자 정보 확인 요청", "ID/PW를 확인 해 주세요!", HttpStatus.BAD_REQUEST),

    // 권한 관련 에러
    AUTHENTICATION_ERROR("ERR3001", "권한 없음", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    
    // 리소스 에러
    RESOURCE_NOT_FOUND("ERR4001", "페이지 찾을 수 없음", "해당 페이지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    
    // 시스템 에러
    INTERNAL_SERVER_ERROR("ERR5001", "시스템 에러", "현재 시스템을 사용할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String title;
    private final String detail;
    private final HttpStatus status;
}
