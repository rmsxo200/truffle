package com.toy.truffle.global.codeEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {
	// 유저영역
	USER_REGISTER_SUCCESS(true, "회원가입 성공"),
	USER_REGISTER_FAILURE(false, "회원가입 실패"),
	USER_LOGIN_SUCCESS(true, "로그인 성공"),

	// FIXME : 이름 기입해주세요
	TRAVEL_SAVE_SUCCESS(true, "정상적으로 저장되었습니다."),
	TRAVEL_SAVE_FAILURE(false, "저장에 실패하였습니다."),
	
	// 여행지 
	DESTINATION_SAVE_SUCCESS(true, "여행지 저장 성공"),
	DESTINATION_SAVE_FAILURE(false, "여행지 저장 실패");

	private final boolean status;
	private final String message;

}
