package com.toy.truffle.travel.controller;

import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.travel.dto.TravelDto;
import com.toy.truffle.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelController {
    private final TravelService travelService;

    /**
     * 여행정보 추가 화면
     */
    @GetMapping("/add")
    public String addTravel() {
        return "travel/addTravel";
    }

    /**
     * 여행정보 저장
     */
    @PostMapping("/info")
    @ResponseBody
    public CommonResponseDTO saveTravel(@RequestBody TravelDto travelDto) {
        return travelService.saveTravel(travelDto);
    }
}
