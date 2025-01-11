package com.toy.truffle.travel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.repository.DestinationRepository;
import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.travel.dto.TravelDto;
import com.toy.truffle.travel.entity.TravelMain;
import com.toy.truffle.travel.entity.TrvlDstnMapping;
import com.toy.truffle.travel.entity.TrvlDstnMappingId;
import com.toy.truffle.travel.repository.TravelMainRepository;
import com.toy.truffle.travel.repository.TrvlDstnMappingRepository;
import com.toy.truffle.travel.service.TravelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TravelServiceTest {
    @InjectMocks
    private TravelService travelService;

    @Mock
    private TravelMainRepository travelMainRepository;

    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private TrvlDstnMappingRepository trvlDstnMappingRepository;

    @Test
    @DisplayName("여행정보 저장")
    public void testSaveTravel() {
        // given
        Long travelSeq = 1L;
        String travelTitle = "여행타이틀";
        LocalDate startDate = LocalDate.of(2024, 10, 10);
        LocalDate endDate = LocalDate.of(2024, 10, 15);
        String createUserId = "tester";
        List<String> destinationCdList = new ArrayList<>(Arrays.asList("10101", "10102"));

        //테스트 DTO 생성
        TravelDto travelDto = TravelDto.builder()
                .travelTitle(travelTitle)
                .startDate(startDate)
                .endDate(endDate)
                .createUserId(createUserId)
                .destinationCdList(destinationCdList)
                .build();

        //저장후 리턴되는 travelMain 엔티티
        TravelMain travelMain = TravelMain.builder()
                .travelSeq(travelSeq)
                .travelTitle(travelTitle)
                .startDate(startDate)
                .endDate(endDate)
                .createUserId(createUserId)
                .build();

        Destination destination1 = new Destination("강동구", "15001");
        Destination destination2 = new Destination("은평구", "10102");

        // when
        // TravelService save 호출 예상동작 설정: stub
        when(travelMainRepository.save(any(TravelMain.class))).thenReturn(travelMain);
        
        // findByDestinationCdIn 의 예상동작 설정
        when(destinationRepository.findByDestinationCdIn(travelDto.getDestinationCdList())).thenReturn(List.of(destination1, destination2));

        // trvlDstnMapping save 호출 예상동작 설정
        when(trvlDstnMappingRepository.saveAll(any())).thenReturn(
                List.of(
                        new TrvlDstnMapping(new TrvlDstnMappingId(1L, "15001"), destination1, travelMain),
                        new TrvlDstnMapping(new TrvlDstnMappingId(1L, "10102"), destination2, travelMain)
                )
        );

        //여행정보 저장 로직 호출
        CommonResponseDTO commonResponseDTO  = travelService.saveTravel(travelDto);

        // then
        // 데이터 저장 상태값 검증
        assertTrue(commonResponseDTO.isStatus());
        // 메시지 내용 검증
        assertEquals(ResponseStatus.TRAVEL_SAVE_SUCCESS.getMessage(), commonResponseDTO.getMessage());

        //travelMainRepository 레포지토리 save 메서드 호출 횟수 확인
        verify(travelMainRepository, times(1)).save(any());

        //destinationRepository 레포지토리 findByDestinationCdIn 메서드 호출 횟수 확인
        verify(destinationRepository, times(1)).findByDestinationCdIn(any());

        //trvlDstnMappingRepository 레포지토리 saveAll 메서드 호출 횟수 확인
        verify(trvlDstnMappingRepository, times(1)).saveAll(any());
    }
}
