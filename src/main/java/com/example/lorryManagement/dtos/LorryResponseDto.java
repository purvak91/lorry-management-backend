package com.example.lorryManagement.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LorryResponseDto {
    private Long lr;
    private String lorryNumber;
    private LocalDate date;
    private String fromLocation;
    private String toLocation;
    private String consignorName;
    private String consignorAddress;
    private String description;
    private  BigDecimal weight;
    private BigDecimal freight;

}
