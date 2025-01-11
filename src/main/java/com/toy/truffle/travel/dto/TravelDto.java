package com.toy.truffle.travel.dto;

import com.toy.truffle.travel.entity.TravelMain;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class TravelDto {
    private Long travelSeq;
    private String travelTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createUserId;
    private List<String> destinationCdList;

    //신규 등록시
    public TravelMain toNewEntity() {
        return TravelMain.builder()
                .travelTitle(travelTitle)
                .startDate(startDate)
                .endDate(endDate)
                .createUserId(createUserId)
                .build();
    }

    //기존 엔티티
    public TravelMain toEntity() {
        return TravelMain.builder()
                .travelSeq(travelSeq)
                .travelTitle(travelTitle)
                .startDate(startDate)
                .endDate(endDate)
                .createUserId(createUserId)
                .build();
    }
}
