package com.toy.truffle.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrvlDstnMappingId implements Serializable {

    @Column(name = "travel_seq")
    private Long travelSeq;

    @Column(name = "destination_cd")
    private String destinationCd;

}
