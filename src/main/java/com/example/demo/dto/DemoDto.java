package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DemoDto {

    private int id;

    @NotBlank
    @NotNull
    @Size(min = 5, max = 15)
    private String postcode;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    private String latitudeDegree;
    private String longitudeDegree;
}
