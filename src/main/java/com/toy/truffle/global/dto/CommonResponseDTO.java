package com.toy.truffle.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponseDTO {

	private final boolean status;
	private final String message;
}
