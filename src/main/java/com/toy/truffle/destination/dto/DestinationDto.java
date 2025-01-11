package com.toy.truffle.destination.dto;

import com.toy.truffle.destination.entity.Destination;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class DestinationDto {
    private String destinationCd;
    private String destinationName;

    public Destination toEntity() {
        return Destination.builder()
                .destinationCd(destinationCd)
                .destinationName(destinationName)
                .build();
    }
}
