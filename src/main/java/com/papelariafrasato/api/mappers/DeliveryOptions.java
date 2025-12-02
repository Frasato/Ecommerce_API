package com.papelariafrasato.api.mappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOptions {

    private Integer id;
    private String name;
    private Double price;
    private String discount;
    private String currency;
    private Integer delivery_time;
    private DeliveryRange delivery_range;
    private List<PackageItem> packages;
    private AdditionalServices additional_services;
    private Company company;
    private boolean has_error;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryRange { private Integer min; private Integer max; }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PackageItem {
        private BigDecimal price;
        private String discount;
        private String format;
        private Dimensions dimensions;
        private BigDecimal weight;
        private BigDecimal insurance_value;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Dimensions {
            private String height;
            private String width;
            private String length;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdditionalServices { private boolean receipt; private boolean own_hand; }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Company { private Integer id; private String name; private String picture; }

}