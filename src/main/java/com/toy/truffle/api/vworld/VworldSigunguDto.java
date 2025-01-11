package com.toy.truffle.api.vworld;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 추가 필드 무시
public class VworldSigunguDto {
    /**
     * vworld API 시군구조회 결과 반환용
     */

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("service")
        private Service service;

        @JsonProperty("status")
        private String status;

        @JsonProperty("page")
        private Page page;

        @JsonProperty("result")
        private Result result;
    }

    @Data
    public static class Service {
        @JsonProperty("name")
        private String name;

        @JsonProperty("version")
        private String version;

        @JsonProperty("operation")
        private String operation;

        @JsonProperty("time")
        private String time;
    }

    @Data
    public static class Page {
        @JsonProperty("total")
        private String total;

        @JsonProperty("current")
        private String current;

        @JsonProperty("size")
        private String size;
    }

    @Data
    public static class Result {
        @JsonProperty("featureCollection")
        private FeatureCollection featureCollection;
    }

    @Data
    public static class FeatureCollection {
        @JsonProperty("features")
        private List<Feature> features;
    }

    @Data
    public static class Feature {
        @JsonProperty("properties")
        private Properties properties;

        @JsonProperty("id")
        private String id;
    }

    @Data
    public static class Properties {
        @JsonProperty("sig_cd")
        private String sigCd;

        @JsonProperty("full_nm")
        private String fullNm;

        @JsonProperty("sig_kor_nm")
        private String sigKorNm;
    }
}