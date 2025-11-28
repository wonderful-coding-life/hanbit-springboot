package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyJson {
    private String name;
    private String phone;
    private String address;
    private LocationJson location;

    @Data
    public static class LocationJson {
        private double x;
        private double y;
    }
}
