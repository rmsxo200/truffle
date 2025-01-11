package com.toy.truffle.travel;

import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.repository.DestinationRepository;
import com.toy.truffle.travel.entity.TravelMain;
import com.toy.truffle.travel.entity.TrvlDstnMapping;
import com.toy.truffle.travel.entity.TrvlDstnMappingId;
import com.toy.truffle.travel.repository.TravelMainRepository;
import com.toy.truffle.travel.repository.TrvlDstnMappingRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrvlDstnMappingRepositoryTest {

    @Autowired
    private TravelMainRepository travelMainRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private TrvlDstnMappingRepository trvlDstnMappingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("여행지, 여행, 매핑테이블 저장")
    public void testSaveTrvlDstnMapping() {
        // given
        //테스트값 세팅
        TravelMain travelMain = TravelMain.builder()
                .travelTitle("여행1")
                .startDate(LocalDate.of(2024, 10, 13))
                .endDate(LocalDate.of(2024, 10, 15))
                .createUserId("user")
                .build();

        Destination destination = Destination.builder()
                .destinationCd("11230")
                .destinationName("은평구")
                .build();

        //TravelMain 와 Destination 먼저 저장
        TravelMain result1 = travelMainRepository.save(travelMain); 
        Destination result2 = destinationRepository.save(destination);

        //저장후 리턴 받은 키값을 세팅
        TrvlDstnMappingId trvlDstnMappingId = new TrvlDstnMappingId(result1.getTravelSeq(), result2.getDestinationCd());

        TrvlDstnMapping trvlDstnMapping = TrvlDstnMapping.builder()
                .id(trvlDstnMappingId)
                .travelMain(result1)
                .destination(result2)
                .build();

        // when
        // 매핑테이블 데이터 저장
        TrvlDstnMapping result3 = trvlDstnMappingRepository.save(trvlDstnMapping);

        //TravelMain에 trvlDstnMapping 추가 (trvlDstnMapping가 연관관계 주인)
        result1.getTrvlDstnMapping().add(result3);

        // then
        // 데이터 저장값 검증
        assertThat(trvlDstnMappingRepository).isNotNull();
        assertThat(result1.getTrvlDstnMapping().size()).isEqualTo(1); // TrvlDstnMapping이 존재하는지 확인
    }

    @Test
    @DisplayName("여행지 삭제로 매핑테이블 삭제 확인")
    public void testDeleteTrvlDstnMapping() {
        // given
        //테스트값 세팅
        TravelMain travelMain = TravelMain.builder()
                .travelTitle("여행1")
                .startDate(LocalDate.of(2024, 10, 13))
                .endDate(LocalDate.of(2024, 10, 15))
                .createUserId("user")
                .build();

        Destination destination = Destination.builder()
                .destinationCd("11230")
                .destinationName("은평구")
                .build();

        //TravelMain 와 Destination 먼저 저장
        TravelMain result1 = travelMainRepository.save(travelMain);
        Destination result2 = destinationRepository.save(destination);

        //저장후 리턴 받은 키값을 세팅
        TrvlDstnMappingId trvlDstnMappingId = new TrvlDstnMappingId(result1.getTravelSeq(), result2.getDestinationCd());

        TrvlDstnMapping trvlDstnMapping = TrvlDstnMapping.builder()
                .id(trvlDstnMappingId)
                .travelMain(result1)
                .destination(result2)
                .build();

        // when
        // 매핑테이블 데이터 저장
        TrvlDstnMapping result3 = trvlDstnMappingRepository.save(trvlDstnMapping);

        //TravelMain에 trvlDstnMapping 추가 (trvlDstnMapping가 연관관계 주인)
        result1.getTrvlDstnMapping().add(result3);

        //TravelMain 삭제
        travelMainRepository.delete(result1);

        //TravelMain 조회
        List<TravelMain> tm = travelMainRepository.findAll();

        //TrvlDstnMapping 조회
        List<TrvlDstnMapping> mp = trvlDstnMappingRepository.findAll();


        // then
        // 데이터 저장값 검증
        assertThat(trvlDstnMappingRepository).isNotNull();
        assertThat(tm.size()).isEqualTo(0); // TravelMain list가 비어있는지 확인
        assertThat(mp.size()).isEqualTo(0); // TrvlDstnMapping list가 비어있는지 확인
    }

    @Test
    @DisplayName("travelMain 내부 list 삭제로 매핑테이블 삭제 확인")
    public void testDeleteTrvlDstnMapping2() {
        // given
        //테스트값 세팅
        TravelMain travelMain = TravelMain.builder()
                .travelTitle("여행1")
                .startDate(LocalDate.of(2024, 10, 13))
                .endDate(LocalDate.of(2024, 10, 15))
                .createUserId("user")
                .build();

        Destination destination = Destination.builder()
                .destinationCd("11230")
                .destinationName("은평구")
                .build();

        //TravelMain 와 Destination 먼저 저장
        TravelMain result1 = travelMainRepository.save(travelMain);
        Destination result2 = destinationRepository.save(destination);

        //저장후 리턴 받은 키값을 세팅
        TrvlDstnMappingId trvlDstnMappingId = new TrvlDstnMappingId(result1.getTravelSeq(), result2.getDestinationCd());

        TrvlDstnMapping trvlDstnMapping = TrvlDstnMapping.builder()
                .id(trvlDstnMappingId)
                .travelMain(result1)
                .destination(result2)
                .build();

        // when
        // 매핑테이블 데이터 저장
        TrvlDstnMapping result3 = trvlDstnMappingRepository.save(trvlDstnMapping);

        em.flush(); //DB 반영
        em.clear(); //영속성 컨텍스트 초기화

        //TravelMain 조회
        TravelMain tm = travelMainRepository.findAll().get(0);

        //자식 리스트 초기화
        tm.getTrvlDstnMapping().clear();

        em.flush(); //DB 반영
        em.clear(); //영속성 컨텍스트 초기화

        //TrvlDstnMapping 조회
        List<TrvlDstnMapping> mp = trvlDstnMappingRepository.findAll();
        
        // then
        // 데이터 저장값 검증
        assertThat(trvlDstnMappingRepository).isNotNull();
        assertThat(mp.size()).isEqualTo(0); // TrvlDstnMapping list가 비어있는지 확인
    }

    @Test
    @Sql(scripts = "/sql/travelMain/travelMainTest.sql") // SQL 파일 실행
    @DisplayName("SQL 파일로 초기 데이터 등록 후 조회 및 수정 테스트")
    @Transactional
    public void testInitData() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM travel_main", Integer.class); //sql파일 데이터가 들어 갔는지 확인

        //List<TrvlDstnMapping> TrvlDstnMapping = trvlDstnMappingRepository.findAll();
        //TrvlDstnMapping 조회 먼저하면 Lazy설정 떄문에 TravelMain가 proxy 객체로 로드되어 내용이 전부 null로 불러와짐

        //TravelMain 조회
        TravelMain travelMain = travelMainRepository.findById(1L).orElseThrow();

        //destination 조회
        Destination destination = destinationRepository.findById("99999").orElseThrow();

        TrvlDstnMappingId trvlDstnMappingId = new TrvlDstnMappingId(travelMain.getTravelSeq(), destination.getDestinationCd());

        TrvlDstnMapping trvlDstnMapping = TrvlDstnMapping.builder()
                .id(trvlDstnMappingId)
                .travelMain(travelMain)
                .destination(destination)
                .build();

        // trvlDstnMapping 조회
        List<TrvlDstnMapping> trvlDstnMappingList = trvlDstnMappingRepository.findAll();

        //리스트에 새로운 trvlDstnMapping 추가
        trvlDstnMappingList.add(trvlDstnMapping);

        //travelMain에 리스트 추가
        travelMain.getTrvlDstnMapping().addAll(trvlDstnMappingList);

        //TravelMain 엔티티 저장
        travelMainRepository.save(travelMain);

        //trvlDstnMapping 조회
        List<TrvlDstnMapping> mp = trvlDstnMappingRepository.findAll();

        // 데이터 저장값 검증
        assertThat(trvlDstnMappingRepository).isNotNull();
        assertThat(mp.size()).isEqualTo(2); // TrvlDstnMapping가 추가 되었는지 확인
    }
}
