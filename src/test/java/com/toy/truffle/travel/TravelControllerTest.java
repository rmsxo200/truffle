package com.toy.truffle.travel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.travel.controller.TravelController;
import com.toy.truffle.travel.dto.TravelDto;
import com.toy.truffle.travel.service.TravelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TravelControllerTest {
    @InjectMocks
    private TravelController travelController;

    @Mock
    private TravelService travelService;

    @Mock
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(travelController).build();
    }

    @Test
    @DisplayName("[Controller] 여행정보 저장")
    public void testSaveTravel() throws Exception {
        // given
        String saveTravelUrl = "/travel/info";

        //테스트 DTO 생성
        TravelDto travelDto = TravelDto.builder()
                .travelTitle("여행타이틀")
                .startDate(LocalDate.of(2024, 10, 10))
                .endDate(LocalDate.of(2024, 10, 15))
                .createUserId("tester")
                .build();

        // when
        // TravelService saveTravel 함수 호출 예상동작 설정: stub
        when(travelService.saveTravel(any(TravelDto.class)))
                .thenReturn(CommonResponseDTO.builder()
                        .status(true)
                        .message(ResponseStatus.TRAVEL_SAVE_SUCCESS.getMessage())
                        .build());

        //http 요청
        ResultActions resultActions = mockMvc.perform(post(saveTravelUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(travelDto)));

        //then
        //응답값 확인
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk()) //200
                .andExpect(jsonPath("$.message").value(ResponseStatus.TRAVEL_SAVE_SUCCESS.getMessage())) //message 값 비교
                .andExpect(jsonPath("$.status").value(true)) //status 값 비교
                .andReturn(); // MvcResult 객체로 리턴
    }



}
