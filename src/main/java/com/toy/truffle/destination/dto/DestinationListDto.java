package com.toy.truffle.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DestinationListDto {
    /**
     * 데이터 타입이 List인 변수밖에 없을 경우 기본생성자가 없으면 애러가 발상
     * 그래서 @Builder 삭제 후 @NoArgsConstructor 추가
     */
    private List<String> destinationCdList;
}
