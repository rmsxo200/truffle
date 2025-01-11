package com.toy.truffle.destination;

import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.repository.DestinationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DestinationRepositoryTest {

    @Autowired
    private DestinationRepository destinationRepository;

    @Test
    @DisplayName("여행지역 테이블 저장")
    public void testSaveDestination() {
        // given
        //테스트값 세팅
        Destination destination = Destination.builder()
                .destinationCd("11230")
                .destinationName("은평구")
                .build();

        // when
        // 데이터 저장
        Destination result = destinationRepository.save(destination);

        // then
        // 데이터 저장값 검증
        assertThat(destinationRepository).isNotNull();
        assertThat(result.getDestinationCd()).isEqualTo("11230");
        assertThat(result.getDestinationName()).isEqualTo("은평구");

    }

    @Test
    @Sql(scripts = "/sql/destination/destinationTest.sql") // SQL 파일 실행
    @DisplayName("여행지역 테이블 조회 (in)")
    public void testFindByDestinationCdIn() {
        // given
        //테스트값 세팅
        List<String> destinationCdList = new ArrayList<>(Arrays.asList("10101", "10102", "15001"));

        // when
        // 데이터 조회
        List<Destination> result = destinationRepository.findByDestinationCdIn(destinationCdList);

        // then
        // 데이터 저장값 검증
        assertThat(destinationRepository).isNotNull();
        assertThat(result.size()).isEqualTo(3);

    }

    @Test
    @Sql(scripts = "/sql/destination/destinationTest.sql") // SQL 파일 실행
    @DisplayName("여행지역 테이블 조회 (like)")
    public void testFindByDestinationCdStartingWith() {
        // given
        //테스트값 세팅
        String firstDestinationCd = "10";

        // when
        // 10으로 시작하는 데이터 조회
        List<Destination> result = destinationRepository.findByDestinationCdStartingWith(firstDestinationCd);

        // then
        // 데이터 저장값 검증
        assertThat(destinationRepository).isNotNull();
        assertThat(result.size()).isEqualTo(4);

    }
}
