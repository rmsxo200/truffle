package com.toy.truffle.destination;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.truffle.destination.controller.DestinationController;
import com.toy.truffle.destination.dto.DestinationDto;
import com.toy.truffle.destination.dto.DestinationListDto;
import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.service.DestinationService;
import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.travel.dto.TravelDto;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DestinationControllerTest {
    @InjectMocks
    private DestinationController destinationController;

    @Mock
    private DestinationService destinationService;

    @Mock
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(destinationController).build();
    }

    @Test
    @DisplayName("[Controller] 여행지 조회")
    public void testFindDestinations() throws Exception {
        // given
        String url = "/dstn/list";

        //테스트 DTO 생성
        DestinationListDto destinationListDto = new DestinationListDto(List.of("10101", "10102", "15001"));

        List<DestinationDto> destinationDtoList = List.of(
                DestinationDto.builder().destinationCd("10101").destinationName("강동구").build(),
                DestinationDto.builder().destinationCd("10102").destinationName("은평구").build(),
                DestinationDto.builder().destinationCd("15001").destinationName("강릉시").build()
        );

        // when
        // TravelService saveTravel 함수 호출 예상동작 설정: stub
        when(destinationService.findDestinations(any())).thenReturn(destinationDtoList);

        //http 요청
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(destinationListDto)));

        //then
        //응답값 확인
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk()) //200
                .andExpect(jsonPath("$.length()").value(3)) //결과 사이즈 비교
                .andReturn(); // MvcResult 객체로 리턴
    }
}
