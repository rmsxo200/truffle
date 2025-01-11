package com.toy.truffle.travel.service;

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
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelService {
    private final TravelMainRepository travelMainRepository;
    private final DestinationRepository destinationRepository;
    private  final TrvlDstnMappingRepository trvlDstnMappingRepository;

    /**
     * 여행 저장
     */
    @Transactional
    public CommonResponseDTO saveTravel(TravelDto travelDto) {
        // TODO : travelDto - createUserId 로그인 정보로 가져오도록 추가 필요
        travelDto.setCreateUserId("test");

        TravelMain travelMain = travelDto.toNewEntity();
        travelMain = travelMainRepository.save(travelMain); //여행 저장 (seq 가져오기 위함)

        List<TrvlDstnMapping> trvlDstnMappingList = new ArrayList<>();

        //여행지역 조회
        List<Destination> destinationList = destinationRepository.findByDestinationCdIn(travelDto.getDestinationCdList());

        for(Destination destination : destinationList) {
            TrvlDstnMappingId trvlDstnMappingId = new TrvlDstnMappingId(travelMain.getTravelSeq(), destination.getDestinationCd());
            
            TrvlDstnMapping trvlDstnMapping = TrvlDstnMapping.builder()
                    .id(trvlDstnMappingId)
                    .travelMain(travelMain)
                    .destination(destination)
                    .build();
            
            trvlDstnMappingList.add(trvlDstnMapping); //여행과 여행지 매핑 정보 추가
        }

        //travelMain.getTrvlDstnMapping().addAll(trvlDstnMappingList); //매핑정보 여행 엔티티에 추가
        //travelMain = travelMainRepository.save(travelMain); //매핑정보 포함상태로 재저장
        trvlDstnMappingRepository.saveAll(trvlDstnMappingList); //매핑정보 저장
        
        ResponseStatus responseStatus = (travelMain != null && travelMain.getTravelSeq() > 0)
                ? ResponseStatus.TRAVEL_SAVE_SUCCESS
                : ResponseStatus.TRAVEL_SAVE_FAILURE;

        return CommonResponseDTO.builder()
                .status(responseStatus.isStatus())
                .message(responseStatus.getMessage())
                .build();
    }
}
