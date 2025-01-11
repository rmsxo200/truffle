package com.toy.truffle.destination.service;

import com.toy.truffle.destination.dto.DestinationDto;
import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.repository.DestinationRepository;
import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.dto.CommonResponseDTO;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;

    /**
     * 여행지역 저장
     */
    public CommonResponseDTO saveDestination(DestinationDto destinationDto) {
        Destination destination = destinationDto.toEntity();

        Destination result = destinationRepository.save(destination);

        ResponseStatus responseStatus = (result != null)
                ? ResponseStatus.DESTINATION_SAVE_SUCCESS
                : ResponseStatus.DESTINATION_SAVE_FAILURE;

        return CommonResponseDTO.builder()
                .status(responseStatus.isStatus())
                .message(responseStatus.getMessage())
                .build();
    }

    /**
     * 여행지역 조회
     */
    public List<DestinationDto> findDestinations(List<String> destinationCdList) {
        List<Destination> destinationList = new ArrayList<>();

        if(destinationCdList != null && destinationCdList.size() > 0) {
            //요청시 destinationCdList가 있다면 해당하는 여행지만 조회
            destinationList = destinationRepository.findByDestinationCdIn(destinationCdList);
        } else {
            //요청시 destinationCdList가 없다면 전체 조회
            destinationList = destinationRepository.findAll();
        }

        // 순환 참조 문제 해결을 위해 DTO로 반환
        List<DestinationDto> result = destinationList.stream()
                .map(o -> DestinationDto.builder()
                        .destinationCd(o.getDestinationCd())
                        .destinationName(o.getDestinationName())
                        .build())
                .collect(Collectors.toList());

        return result;
    }
}
