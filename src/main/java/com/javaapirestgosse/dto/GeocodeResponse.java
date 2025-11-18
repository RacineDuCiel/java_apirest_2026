package com.javaapirestgosse.dto;

import lombok.Data;
import java.util.List;

@Data
public class GeocodeResponse {
    private String type;
    private String version;
    private List<Feature> features;

    @Data
    public static class Feature {
        private String type;
        private Geometry geometry;
        private Properties properties;
    }

    @Data
    public static class Geometry {
        private String type;
        private List<Double> coordinates;
    }

    @Data
    public static class Properties {
        private String label;
        private Double score;
        private String housenumber;
        private String name;
        private String postcode;
        private String city;
    }
}
