package com.toy.truffle.api;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
public class ApiClientTest {
    private MockWebServer mockWebServer;
    private ApiClient apiClient;

    @BeforeEach
    void setUp() throws IOException {
        /**
         * MockWebServer : Square 팀에서 만든 MOCK 웹서버
         */

        mockWebServer = new MockWebServer();
        mockWebServer.start();  // 서버 시작

        String mockBaseUrl = mockWebServer.url("/").toString();

        // RestClient와 ApiClient 설정
        RestClient restClient = RestClient.builder()
                .baseUrl(mockBaseUrl)
                .build();

        apiClient = new ApiClient(restClient); // ApiClient 생성
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();  // 서버 종료. 인스턴스가 재사용 되어서는 안 됨
    }

    @Test
    @DisplayName("GET 요청 테스트")
    public void testGetMethod() {
        // 가짜 서버인 mockWebServer 호출시 응답값
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"resultData\": \"test success!\"}")
                .addHeader("Content-Type", "application/json"));

        String url = mockWebServer.url("/test/test").toString();  // 임의의 url

        //호출
        ResponseEntity<Map> result = apiClient.get(url, Map.class, new HashMap<>());

        // 응답 검증
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().get("resultData")).isEqualTo("test success!");
    }

    @Test
    @DisplayName("POST 요청 테스트")
    public void testPostMethod() {
        // TODU :: 작성필요
    }

}
