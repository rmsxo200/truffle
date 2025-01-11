package com.toy.truffle.travel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelMain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelSeq;

    @Column
    private String travelTitle;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String createUserId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDttm;

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updateDttm;

    //TODO :: updateDttm insert할때도 값이 들어가는 문제 해결 필요

    @OneToMany(mappedBy = "travelMain", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrvlDstnMapping> trvlDstnMapping = new ArrayList<>(); // trvlDstnMapping가 연관관계 주인

    @Builder
    public TravelMain(long travelSeq, String travelTitle, LocalDate startDate, LocalDate endDate, String createUserId) {
        this.travelSeq = travelSeq;
        this.travelTitle = travelTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createUserId = createUserId;
    }
}
