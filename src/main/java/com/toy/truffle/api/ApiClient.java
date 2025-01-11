package com.toy.truffle.api;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.MediaType;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiClient {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestClient restClient;

    //시군구 조회 공공 API
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, String> headers) {
        try {
            // 요청 헤더 설정
            HttpHeaders httpHeaders = new HttpHeaders();
            headers.forEach(httpHeaders::set);

            // exchange를 사용한 GET 요청
            return restClient
                    .method(HttpMethod.GET)
                    .uri(url)
                    .headers(h -> h.addAll(httpHeaders))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new HttpClientErrorException(response.getStatusCode(), "4xx 클라이언트 오류 발생");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new HttpServerErrorException(response.getStatusCode(), "5xx 서버 오류 발생");
                    })
                    .toEntity(responseType);

        } catch (HttpClientErrorException e) {
            logger.info("클라이언트 오류: " + e.getStatusCode() + ", 메시지: " + e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            logger.info("서버 오류: " + e.getStatusCode() + ", 메시지: " + e.getMessage());
            throw e;
        } catch (RestClientResponseException e) { // 기타 RestClient 오류 처리
            logger.info("RestClient 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) { // 알 수 없는 오류 처리
            logger.info("알 수 없는 오류 발생: {}", e.getMessage());
            throw new RuntimeException("알 수 없는 오류 발생", e);
        }
    }

    public <T, R> ResponseEntity<T> post(String url, R requestBody, Class<T> responseType, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            headers.forEach(httpHeaders::set);

            // exchange를 사용한 POST 요청
            return restClient
                    .method(HttpMethod.POST)
                    .uri(url)
                    .headers(h -> h.addAll(httpHeaders))
                    .body(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new HttpClientErrorException(response.getStatusCode(), "4xx 클라이언트 오류 발생");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new HttpServerErrorException(response.getStatusCode(), "5xx 서버 오류 발생");
                    })
                    .toEntity(responseType);

        } catch (HttpClientErrorException e) {
            logger.info("클라이언트 오류: " + e.getStatusCode() + ", 메시지: " + e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            logger.info("서버 오류: " + e.getStatusCode() + ", 메시지: " + e.getMessage());
            throw e;
        } catch (RestClientResponseException e) {
            logger.info("RestClient 오류: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.info("알 수 없는 오류 발생: " + e.getMessage());
            throw new RuntimeException("알 수 없는 오류 발생", e);
        }
    }
}
