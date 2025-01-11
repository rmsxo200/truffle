package com.toy.truffle.destination.controller;

import com.toy.truffle.destination.dto.DestinationDto;
import com.toy.truffle.destination.dto.DestinationListDto;
import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.service.DestinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/dstn")
public class DestinationController {
    private final DestinationService destinationService;

    /**
     * 여행정보 저장
     */
    @PostMapping("/list")
    @ResponseBody
    public List<DestinationDto> findDestinations(@RequestBody(required=false) DestinationListDto destinationListDto) {
        List<String> destinationCdList = (destinationListDto != null ? destinationListDto.getDestinationCdList() : null);
        List<DestinationDto> destination = destinationService.findDestinations(destinationCdList);
        return destination;
    }
}
