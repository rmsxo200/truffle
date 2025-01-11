package com.toy.truffle.api.vworld;

import com.toy.truffle.api.ApiClient;
import com.toy.truffle.destination.entity.Destination;
import com.toy.truffle.destination.repository.DestinationRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VworldApiService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ApiClient apiClient;
    private final DestinationRepository destinationRepository;

    @Value("${vworld.url}")
    private String vworldUrl;

    @Transactional
    public void getSigungu(String sidoFirstCd) {
        /**
         * 모든 시군구를 일괄로 가져올 수 없어 시도코드 앞자리인 1~5를 순차적으로 호출하여야 함.
         * 서비스키는 발급후 일정기간만 사용가능 하여 만료되면 재발급 필요
         * 호출 API 설명 : https://www.vworld.kr/dev/v4dv_2ddataguide2_s002.do?svcIde=adsigg
         */

        StringBuffer url = new StringBuffer(vworldUrl);
        url.append("/req/data"); //path
        url.append("?service=data"); // 요청 서비스명
        url.append("&version=2.0"); // 요청 서비스 버전
        url.append("&request=GetFeature"); // 요청 서비스 오퍼레이션
        url.append("&format=json"); // 응답결과 포맷
        url.append("&size=300"); // 한 페이지에 출력될 응답결과 건수
        url.append("&page=1"); // 응답결과 페이지 번호
        url.append("&data=LT_C_ADSIGG_INFO"); // 조회할 데이터
        url.append("&geometry=false"); // 지오메트리 반환 여부 (true(기본값), false)
        url.append("&attribute=true"); // 속성 반환 여부
        url.append("&domain=localhost:8080"); //요청 URL (vworld에 설정한 주소)
        url.append("&key=991FDC65-36E5-3CDD-8A4E-9230EE80A117"); // 발급받은 서비스키
        url.append("&attrfilter=sig_cd:like:" + sidoFirstCd); // 조회조건 (시군수코드 like 문)
        url.append("&columns=sig_cd,full_nm,sig_kor_nm"); // 응답결과로 받기를 원하는 컬럼, 생략 시 전체 컬럼 반환

        try {
            Map<String, String> headers = new HashMap<>();
            //headers.put("Authorization", "Bearer token");

            //API 호출
            ResponseEntity<VworldSigunguDto> response = apiClient.get(url.toString(), VworldSigunguDto.class, headers);

            // 성공시
            if(200 == response.getStatusCode().value()) {
                List<Destination> destinationList = new ArrayList<>();
                VworldSigunguDto responseBody = response.getBody();
                List<VworldSigunguDto.Feature> Features = responseBody.getResponse().getResult().getFeatureCollection().getFeatures();

                for(VworldSigunguDto.Feature Feature : Features) {
                    //API 응답결과로 Destination 엔티티 생성
                    Destination destination = Destination.builder()
                            .destinationCd(Feature.getProperties().getSigCd())
                            .destinationName(Feature.getProperties().getSigKorNm())
                            .build();

                    // list에 적재
                    destinationList.add(destination);
                }

                //destination 삭제
                List<Destination> deleteList = destinationRepository.findByDestinationCdStartingWith(sidoFirstCd);
                destinationRepository.deleteAll(deleteList);

                //destination 저장
                List<Destination> resultList = destinationRepository.saveAll(destinationList);
                logger.info("[성공] 저장 시군구수 : " + resultList.size());
            } else {
                logger.info("[실패] 응답상태 : " + response.getStatusCode());
                logger.info("[실패] 응답결과 : {}", response.getBody().getResponse());
            }

        } catch (RuntimeException e) {
            logger.info("[실패] 오류 발생: " + e.getMessage());
        }
    }
}
